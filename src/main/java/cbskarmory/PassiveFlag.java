package cbskarmory;

public class PassiveFlag {
	public enum MoveType{
			FOOT, 
			TIRES, 
			TREADS, 
			SEA, 
			AIR,
			LANDER
	}
	public enum COFlag{
            COST,
            ATTACK,
            DEFENSE,
            COUNTER,
            MOVE,
            DAILY_COST,
            STEALTH_COST,
            LUCK,
            CAPTURE
	}

	public enum UnitType{
            //land
            INFANTRY,
            MECH,
            RECON,
            APC,
            TANK,
            ARTILLERY,
            MEDIUM_TANK,
            ANTI_AIR,
            ROCKETS,
            MISSILES,
            HEAVY_TANK,
            HOVER_TANK,

            //air
            T_COPTER,
            B_COPTER,
            FIGHTER,
            BOMBER,
            CAS,
            S_COPTER,
            STEALTH_BOMBER,
            DROPSHIP,
            ADVANCED_FIGHTER,
            JSF,

            //sea
            LANDER,
            CRUISER,
            SUB,
            BATTLESHIP,
            CARRIER,
            ICBM_SUB
	}

	COFlag coFlag;
	UnitType unitType;
	public PassiveFlag(COFlag f) {
		this.coFlag = f;
	}
	public PassiveFlag(UnitType u) {
		this.unitType = u;
	}
}
