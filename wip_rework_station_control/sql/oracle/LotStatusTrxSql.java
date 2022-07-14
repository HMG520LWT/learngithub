package kyber.wip_rework_station_control.sql.oracle;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.annotation.SqlExecute;
import com.kyber.core.constants.SystemConstants;
import com.kyber.core.exception.LogicalException;
import com.kyber.core.exception.MESException;
import com.kyber.core.sql.KyberSqlProxy;
import com.kyber.core.sql.SqlBean;
import com.kyber.core.util.CheckUtil;

import kyber.wip_rework_station.sql.action.LotTrxSqlAction;

@Profile(SystemConstants.DB_PROFILE_ORACLE)
@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LotStatusTrxSql  extends KyberSqlProxy{
    
	/**
	 * update lot_status 
	 * @param siteId
	 * @param lotId
	 * @return
	 * @throws Exception
	 */
	@SqlExecute(LotTrxSqlAction.UPDATE_LOT_STATUS)
	public int updateLotStatus(String lotId) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		// writer update lot_status sql
		sql.append("update r_lot set lot_status='MVIN' where ");
		
		// lot_id not null
		if(!CheckUtil.isNull(lotId)) {
			sql.append(" lot_id=? ");
		}else {
			
			// 参数值不存在
			throw new LogicalException("E000009");
		}
		

		SqlBean sqlBean = sqlBeanExecuter.createSqlBean(sql.toString(), log);
		
		if(!CheckUtil.isNull(lotId)) {
			sqlBean.addParameter(lotId);
		}
		
		
		return sqlBean.executeUpdate();
	}
}
