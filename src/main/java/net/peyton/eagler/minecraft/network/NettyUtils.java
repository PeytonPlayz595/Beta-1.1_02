package net.peyton.eagler.minecraft.network;

import io.netty.buffer.AbstractByteBuf;
import io.netty.buffer.ByteBufUtil;

public class NettyUtils {

	private static final byte WRITE_UTF_UNKNOWN = (byte) '?';

	public static int writeUtf8(AbstractByteBuf buffer, CharSequence seq, int len) {
		int utfLen = 0;
		for (int i = 0; i < len; i++) {
			char c = seq.charAt(i);
			if (c == 0) {
				utfLen += 2;
			} else if (c < 0x80) {
				utfLen++;
			} else if (c < 0x800) {
				utfLen += 2;
			} else if (c >= '\uD800' && c <= '\uDFFF') {
				if (!Character.isHighSurrogate(c)) {
					utfLen += 1;
					continue;
				}
				try {
					char c2 = seq.charAt(++i);
					if (!Character.isLowSurrogate(c2)) {
						utfLen += 2;
						continue;
					}
					utfLen += 4;
				} catch (IndexOutOfBoundsException e) {
					utfLen += 1;
					break;
				}
			} else {
				utfLen += 3;
			}
		}

		buffer.ensureWritable(2 + utfLen);
		int oldWriterIndex = buffer.writerIndex();
		int writerIndex = oldWriterIndex;

		ByteBufUtil._setByte(buffer, writerIndex++, (byte) (utfLen >>> 8));
		ByteBufUtil._setByte(buffer, writerIndex++, (byte) utfLen);

		for (int i = 0; i < len; i++) {
			char c = seq.charAt(i);
			if (c == 0) {
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) 0xC0);
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) 0x80);
			} else if (c < 0x80) {
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) c);
			} else if (c < 0x800) {
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0xc0 | (c >> 6)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | (c & 0x3f)));
			} else if (c >= '\uD800' && c <= '\uDFFF') {
				if (!Character.isHighSurrogate(c)) {
					ByteBufUtil._setByte(buffer, writerIndex++, WRITE_UTF_UNKNOWN);
					continue;
				}
				final char c2;
				try {
					c2 = seq.charAt(++i);
				} catch (IndexOutOfBoundsException e) {
					ByteBufUtil._setByte(buffer, writerIndex++, WRITE_UTF_UNKNOWN);
					break;
				}
				if (!Character.isLowSurrogate(c2)) {
					ByteBufUtil._setByte(buffer, writerIndex++, WRITE_UTF_UNKNOWN);
					ByteBufUtil._setByte(buffer, writerIndex++, Character.isHighSurrogate(c2) ? WRITE_UTF_UNKNOWN : c2);
					continue;
				}
				int codePoint = Character.toCodePoint(c, c2);
				// See http://www.unicode.org/versions/Unicode7.0.0/ch03.pdf#G2630.
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0xf0 | (codePoint >> 18)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | ((codePoint >> 12) & 0x3f)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | ((codePoint >> 6) & 0x3f)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | (codePoint & 0x3f)));
			} else {
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0xe0 | (c >> 12)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | ((c >> 6) & 0x3f)));
				ByteBufUtil._setByte(buffer, writerIndex++, (byte) (0x80 | (c & 0x3f)));
			}
		}

		// writerIndex is updated without any extra checks for performance reasons
		ByteBufUtil.setWriterIndex(buffer, writerIndex);
		return writerIndex - oldWriterIndex;
	}

	public static String readUtf8(AbstractByteBuf buffer) {
		int utflen = buffer.readUnsignedShort();

		if (utflen == 0) {
			return "";
		}

		int oldReaderIndex = buffer.readerIndex();
		int readerIndex = oldReaderIndex;
		int endIdx = readerIndex + utflen;

		char[] arr = new char[utflen];
		int idx = 0;

		while (readerIndex < endIdx) {
			int b1 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;

			if (b1 < 0x80) {
				arr[idx++] = (char) b1;
			} else if ((b1 & 0xE0) == 0xC0) {
				int b2 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;

				if (b1 == 0xC0 && b2 == 0x80) {
					arr[idx++] = 0;
				} else {
					arr[idx++] = (char) (((b1 & 0x1F) << 6) | (b2 & 0x3F));
				}
			} else if ((b1 & 0xF0) == 0xE0) {
				int b2 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;
				int b3 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;

				arr[idx++] = (char) (((b1 & 0x0F) << 12) | ((b2 & 0x3F) << 6) | (b3 & 0x3F));
			} else if ((b1 & 0xF8) == 0xF0) {
				int b2 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;
				int b3 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;
				int b4 = ByteBufUtil._getByte(buffer, readerIndex++) & 0xFF;

				int codePoint = ((b1 & 0x07) << 18) | ((b2 & 0x3F) << 12) | ((b3 & 0x3F) << 6) | (b4 & 0x3F);

				codePoint -= 0x10000;
				arr[idx++] = (char) (0xD800 | (codePoint >> 10));
				arr[idx++] = (char) (0xDC00 | (codePoint & 0x3FF));
			} else {
				arr[idx++] = '\uFFFD';
			}
		}

		ByteBufUtil.setReaderIndex(buffer, endIdx);
		return new String(arr, 0, idx);
	}
}
