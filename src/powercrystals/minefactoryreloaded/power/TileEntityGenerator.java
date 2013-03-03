package powercrystals.minefactoryreloaded.power;

import net.minecraft.tileentity.TileEntity;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import powercrystals.core.position.BlockPosition;
import powercrystals.core.power.PowerProviderAdvanced;
import powercrystals.minefactoryreloaded.core.TileEntityFactory;

public abstract class TileEntityGenerator extends TileEntityFactory implements IPowerReceptor
{
	private IPowerProvider _powerProvider;
	
	protected TileEntityGenerator()
	{
		_powerProvider = new PowerProviderAdvanced();
		_powerProvider.configure(0, 0, 0, 0, 0);
	}
	
	protected final int producePower(int mj)
	{
		BlockPosition ourbp = BlockPosition.fromFactoryTile(this);
		
		for(BlockPosition bp : ourbp.getAdjacent(true))
		{
			TileEntity te = worldObj.getBlockTileEntity(bp.x, bp.y, bp.z);
			if(te == null || !(te instanceof IPowerReceptor))
			{
				continue;
			}
			
			IPowerReceptor ipr = ((IPowerReceptor)te);
			IPowerProvider pp = ipr.getPowerProvider();
			if(pp != null && pp.preConditions(ipr) && pp.getMinEnergyReceived() <= mj)
			{
				int mjUsed = Math.min(Math.min(pp.getMaxEnergyReceived(), mj), pp.getMaxEnergyStored() - (int)Math.floor(pp.getEnergyStored()));
				pp.receiveEnergy(mjUsed, bp.orientation);
				
				mj -= mjUsed;
				if(mj <= 0)
				{
					return 0;
				}
			}
		}
		
		return mj;
	}

	@Override
	public void setPowerProvider(IPowerProvider provider)
	{
		_powerProvider = provider;
	}

	@Override
	public IPowerProvider getPowerProvider()
	{
		return _powerProvider;
	}

	@Override
	public void doWork()
	{
	}

	@Override
	public int powerRequest()
	{
		return 0;
	}
}
