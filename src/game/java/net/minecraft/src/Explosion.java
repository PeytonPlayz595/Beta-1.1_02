package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.lax1dude.eaglercraft.Random;
import java.util.Set;

public class Explosion {
	public boolean field_12257_a = false;
	private Random field_12250_h = new Random();
	private World field_12249_i;
	public double field_12256_b;
	public double field_12255_c;
	public double field_12254_d;
	public Entity field_12253_e;
	public float field_12252_f;
	public Set<ChunkPosition> field_12251_g = new HashSet<>();

	public Explosion(World var1, Entity var2, double var3, double var5, double var7, float var9) {
		this.field_12249_i = var1;
		this.field_12253_e = var2;
		this.field_12252_f = var9;
		this.field_12256_b = var3;
		this.field_12255_c = var5;
		this.field_12254_d = var7;
	}

	public void func_12248_a() {
		float var1 = this.field_12252_f;
		byte var2 = 16;

		int var3;
		int var4;
		int var5;
		double var15;
		double var17;
		double var19;
		for(var3 = 0; var3 < var2; ++var3) {
			for(var4 = 0; var4 < var2; ++var4) {
				for(var5 = 0; var5 < var2; ++var5) {
					if(var3 == 0 || var3 == var2 - 1 || var4 == 0 || var4 == var2 - 1 || var5 == 0 || var5 == var2 - 1) {
						double var6 = (double)((float)var3 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var8 = (double)((float)var4 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var10 = (double)((float)var5 / ((float)var2 - 1.0F) * 2.0F - 1.0F);
						double var12 = Math.sqrt(var6 * var6 + var8 * var8 + var10 * var10);
						var6 /= var12;
						var8 /= var12;
						var10 /= var12;
						float var14 = this.field_12252_f * (0.7F + this.field_12249_i.rand.nextFloat() * 0.6F);
						var15 = this.field_12256_b;
						var17 = this.field_12255_c;
						var19 = this.field_12254_d;

						for(float var21 = 0.3F; var14 > 0.0F; var14 -= var21 * (12.0F / 16.0F)) {
							int var22 = MathHelper.floor_double(var15);
							int var23 = MathHelper.floor_double(var17);
							int var24 = MathHelper.floor_double(var19);
							int var25 = this.field_12249_i.getBlockId(var22, var23, var24);
							if(var25 > 0) {
								var14 -= (Block.blocksList[var25].getExplosionResistance(this.field_12253_e) + 0.3F) * var21;
							}

							if(var14 > 0.0F) {
								this.field_12251_g.add(new ChunkPosition(var22, var23, var24));
							}

							var15 += var6 * (double)var21;
							var17 += var8 * (double)var21;
							var19 += var10 * (double)var21;
						}
					}
				}
			}
		}

		this.field_12252_f *= 2.0F;
		var3 = MathHelper.floor_double(this.field_12256_b - (double)this.field_12252_f - 1.0D);
		var4 = MathHelper.floor_double(this.field_12256_b + (double)this.field_12252_f + 1.0D);
		var5 = MathHelper.floor_double(this.field_12255_c - (double)this.field_12252_f - 1.0D);
		int var29 = MathHelper.floor_double(this.field_12255_c + (double)this.field_12252_f + 1.0D);
		int var7 = MathHelper.floor_double(this.field_12254_d - (double)this.field_12252_f - 1.0D);
		int var30 = MathHelper.floor_double(this.field_12254_d + (double)this.field_12252_f + 1.0D);
		List<Entity> var9 = this.field_12249_i.getEntitiesWithinAABBExcludingEntity(this.field_12253_e, AxisAlignedBB.getBoundingBoxFromPool((double)var3, (double)var5, (double)var7, (double)var4, (double)var29, (double)var30));
		Vec3D var31 = Vec3D.createVector(this.field_12256_b, this.field_12255_c, this.field_12254_d);

		for(int var11 = 0; var11 < var9.size(); ++var11) {
			Entity var33 = (Entity)var9.get(var11);
			double var13 = var33.getDistance(this.field_12256_b, this.field_12255_c, this.field_12254_d) / (double)this.field_12252_f;
			if(var13 <= 1.0D) {
				var15 = var33.posX - this.field_12256_b;
				var17 = var33.posY - this.field_12255_c;
				var19 = var33.posZ - this.field_12254_d;
				double var39 = (double)MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
				var15 /= var39;
				var17 /= var39;
				var19 /= var39;
				double var40 = (double)this.field_12249_i.func_675_a(var31, var33.boundingBox);
				double var41 = (1.0D - var13) * var40;
				var33.attackEntityFrom(this.field_12253_e, (int)((var41 * var41 + var41) / 2.0D * 8.0D * (double)this.field_12252_f + 1.0D));
				var33.motionX += var15 * var41;
				var33.motionY += var17 * var41;
				var33.motionZ += var19 * var41;
			}
		}

		this.field_12252_f = var1;
		ArrayList<ChunkPosition> var32 = new ArrayList<>();
		var32.addAll(this.field_12251_g);
		if(this.field_12257_a) {
			for(int var34 = var32.size() - 1; var34 >= 0; --var34) {
				ChunkPosition var35 = (ChunkPosition)var32.get(var34);
				int var36 = var35.x;
				int var37 = var35.y;
				int var16 = var35.z;
				int var38 = this.field_12249_i.getBlockId(var36, var37, var16);
				int var18 = this.field_12249_i.getBlockId(var36, var37 - 1, var16);
				if(var38 == 0 && Block.opaqueCubeLookup[var18] && this.field_12250_h.nextInt(3) == 0) {
					this.field_12249_i.setBlockWithNotify(var36, var37, var16, Block.fire.blockID);
				}
			}
		}

	}

	public void func_12247_b() {
		this.field_12249_i.playSoundEffect(this.field_12256_b, this.field_12255_c, this.field_12254_d, "random.explode", 4.0F, (1.0F + (this.field_12249_i.rand.nextFloat() - this.field_12249_i.rand.nextFloat()) * 0.2F) * 0.7F);
		ArrayList<ChunkPosition> var1 = new ArrayList<>();
		var1.addAll(this.field_12251_g);

		for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
			ChunkPosition var3 = (ChunkPosition)var1.get(var2);
			int var4 = var3.x;
			int var5 = var3.y;
			int var6 = var3.z;
			int var7 = this.field_12249_i.getBlockId(var4, var5, var6);

			for(int var8 = 0; var8 < 1; ++var8) {
				double var9 = (double)((float)var4 + this.field_12249_i.rand.nextFloat());
				double var11 = (double)((float)var5 + this.field_12249_i.rand.nextFloat());
				double var13 = (double)((float)var6 + this.field_12249_i.rand.nextFloat());
				double var15 = var9 - this.field_12256_b;
				double var17 = var11 - this.field_12255_c;
				double var19 = var13 - this.field_12254_d;
				double var21 = (double)MathHelper.sqrt_double(var15 * var15 + var17 * var17 + var19 * var19);
				var15 /= var21;
				var17 /= var21;
				var19 /= var21;
				double var23 = 0.5D / (var21 / (double)this.field_12252_f + 0.1D);
				var23 *= (double)(this.field_12249_i.rand.nextFloat() * this.field_12249_i.rand.nextFloat() + 0.3F);
				var15 *= var23;
				var17 *= var23;
				var19 *= var23;
				this.field_12249_i.spawnParticle("explode", (var9 + this.field_12256_b * 1.0D) / 2.0D, (var11 + this.field_12255_c * 1.0D) / 2.0D, (var13 + this.field_12254_d * 1.0D) / 2.0D, var15, var17, var19);
				this.field_12249_i.spawnParticle("smoke", var9, var11, var13, var15, var17, var19);
			}

			if(var7 > 0) {
				Block.blocksList[var7].dropBlockAsItemWithChance(this.field_12249_i, var4, var5, var6, this.field_12249_i.getBlockMetadata(var4, var5, var6), 0.3F);
				this.field_12249_i.setBlockWithNotify(var4, var5, var6, 0);
				Block.blocksList[var7].onBlockDestroyedByExplosion(this.field_12249_i, var4, var5, var6);
			}
		}

	}
}
