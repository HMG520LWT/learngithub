package kyber.wip_rework_station_control.sql.oracle;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.annotation.SqlExecute;
import com.kyber.core.constants.SystemConstants;
import com.kyber.core.exception.LogicalException;
import com.kyber.core.sql.KyberSqlProxy;
import com.kyber.core.sql.SqlBean;
import com.kyber.core.util.CheckUtil;
import kyber.wip_rework_station.sql.action.ReworkTrxSqlAction;

@Profile(SystemConstants.DB_PROFILE_ORACLE)
@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReworkTrxSql extends KyberSqlProxy {

	@SqlExecute(ReworkTrxSqlAction.INSERT_REWORK_TABLE)

	public int addRworkStation( String siteId,String lotId, String productName, String productVer, String stepName, 
			String qty,
			String equipmentId, String reworkReason, String reworkCodeId,  String createUser, String lmUser)
			throws Exception {
		StringBuffer sql = new StringBuffer();


		sql.append(" INSERT INTO R_REWORK_STATION ");
		sql.append(" (lot_id, product_name,product_ver, step_name, qty,equipment_id,rework_reason,rework_code_id,create_user,lm_user) ");
		sql.append(" values(?,?,?,?,?,?,?,?,?,?)");
        
		// ��sqlע���sqlBean
		SqlBean sqlBean = sqlBeanExecuter.createSqlBean(sql.toString(), log);

		if (!CheckUtil.isNull(lotId)) {
			sqlBean.addParameter(lotId);
		}else {
			throw new LogicalException("E000003");
		}
		
		// �������ݵĳ�ʼֵ
		sqlBean.addParameter(!CheckUtil.isNull(productName)? productName:null);
		sqlBean.addParameter(!CheckUtil.isNull(productVer)? productVer:null);
		sqlBean.addParameter(!CheckUtil.isNull(stepName)? stepName:null);
		sqlBean.addParameter(!CheckUtil.isNull(qty)? qty:null);
		sqlBean.addParameter(!CheckUtil.isNull(equipmentId)? equipmentId:null);
		sqlBean.addParameter(!CheckUtil.isNull(reworkReason)?  reworkReason:null);
		sqlBean.addParameter(!CheckUtil.isNull(reworkCodeId)?  reworkCodeId:null);
		sqlBean.addParameter(!CheckUtil.isNull(createUser)?  createUser:null);
		sqlBean.addParameter(!CheckUtil.isNull(lmUser)?  lmUser:null);
		
		return sqlBean.executeUpdate();

	}

	// �޸ķ�������
	@SqlExecute(ReworkTrxSqlAction.UPDATE_REWORK_TABLE)
	public int updateReworkQty(String qty, String lotId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" update R_REWORK_STATION set qty=? ");
		sql.append(" where lot_id = ?");

		SqlBean sqlBean = sqlBeanExecuter.createSqlBean(sql.toString(), log);
		sqlBean.addParameter(qty);
		sqlBean.addParameter(lotId);
		
		return sqlBean.executeUpdate();
	}
	
	// 更新操作
	@SqlExecute(ReworkTrxSqlAction.DELETE_REWORK_TABLE)
	public int deleteReworkStation(String lotId) throws Exception {
		
		//  设置sql
		StringBuffer sql = new StringBuffer();
		sql.append(" delete r_rework_station ");
		sql.append(" where lot_id = ?");

		SqlBean sqlBean = sqlBeanExecuter.createSqlBean(sql.toString(), log);
		sqlBean.addParameter(lotId);
		
		return sqlBean.executeUpdate();
	}
	
	

}
