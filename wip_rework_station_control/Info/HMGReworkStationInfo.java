package kyber.wip_rework_station_control.Info;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.exception.LogicalException;
import com.kyber.core.runtime.LogicalService;
import com.kyber.core.sql.KyberSqlProxy;
import com.kyber.core.util.CheckUtil;

import kyber.wip_rework_station.sql.action.LotTrxSqlAction;
import kyber.wip_rework_station.sql.action.ReworkQuerySqlAction;
import kyber.wip_rework_station.sql.action.ReworkTrxSqlAction;
import lombok.Data;

@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HMGReworkStationInfo extends LogicalService {

	// 定义我们传入的动作
	private String ADD_FLAG = "ADD";
	private String UPDATE_FLAG = "UPDATE";
	private String DELETE_FLAG = "DELETE";
	private String GET_REWORK_STATION="GET_REWORK_STATION";

	private InputSpo _inSpo;
	private OutputSpo _outSpo = new OutputSpo();

	@Autowired
	@Qualifier("hmgReworkSql")
	private KyberSqlProxy hMGReworkSql;

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		
		// 下面对取到的参数进行逻辑判断
		/*
		 * if(CheckUtil.isNull(_inSpo.getSiteId())) { throw new
		 * LogicalException("E000016"); }
		 */
        
        if(CheckUtil.isNull(_inSpo.getLotId())) {
			throw new LogicalException("E000003");// Lot ID is null.
		}
        
        // 如果传入的参数为空，就设置初始值
        _inSpo.setCreateUser(!CheckUtil.isNull(_inSpo.getCreateUser())?_inSpo.getCreateUser():null);
        _inSpo.setLmUser(!CheckUtil.isNull(_inSpo.getLmUser())?_inSpo.getLmUser():null);
        _inSpo.setEquipmentId(!CheckUtil.isNull(_inSpo.getEquipmentId())?_inSpo.getEquipmentId():null);
        _inSpo.setProductName(!CheckUtil.isNull(_inSpo.getProductName())?_inSpo.getProductName():null);
        _inSpo.setProductVer(!CheckUtil.isNull(_inSpo.getProductVer())?_inSpo.getProductVer():null);
        _inSpo.setQty(!CheckUtil.isNull(_inSpo.getQty())?_inSpo.getQty():null);
        _inSpo.setReworkCodeId(!CheckUtil.isNull(_inSpo.getReworkCodeId())?_inSpo.getReworkCodeId():null);
        _inSpo.setReworkReason(!CheckUtil.isNull(_inSpo.getReworkReason())?_inSpo.getReworkReason():null);
        _inSpo.setStepName(!CheckUtil.isNull(_inSpo.getStepName())?_inSpo.getStepName():null);
        _inSpo.setActionFlag(!CheckUtil.isNull(_inSpo.getActionFlag())?_inSpo.getActionFlag():null);


        
		if(ADD_FLAG.equals(_inSpo.actionFlag)) {
			
		   // 执行修改lotstatus信息
			int updateLotStatusIndex = hMGReworkSql.executeUpdate(LotTrxSqlAction.UPDATE_LOT_STATUS, log,_inSpo.getLotId());
			
			if(updateLotStatusIndex<1) {
				throw new LogicalException("SYSF000001"); // update is error
			}
			
			
			// 执行添加方法
			int insertIndex = hMGReworkSql.executeUpdate(ReworkTrxSqlAction.INSERT_REWORK_TABLE, log,
					_inSpo.getSiteId(),
					_inSpo.getLotId(),
					_inSpo.getProductName(),_inSpo.getProductVer(),_inSpo.getStepName(),_inSpo.getQty(),
					_inSpo.getEquipmentId(),_inSpo.getReworkReason(),_inSpo.getReworkCodeId(),_inSpo.getCreateUser(),_inSpo.getLmUser());
			if(insertIndex<1) {
				throw new LogicalException("SYSF000001"); // 添加失败
			}

		}
		
		
		// 判断是否时update
		if(UPDATE_FLAG.equals(_inSpo.actionFlag)) {
			// 执行更新qty
			int updateReworkIndex = hMGReworkSql.executeUpdate(ReworkTrxSqlAction.UPDATE_REWORK_TABLE, log,_inSpo.getQty(), _inSpo.getLotId());
			if(updateReworkIndex<1) {
				throw new LogicalException("SYSF000001");
			}
		}
		
		// 删除rework_station
		
		if(DELETE_FLAG.equals(_inSpo.actionFlag)) {
			int deleteIndex = hMGReworkSql.executeUpdate(ReworkTrxSqlAction.DELETE_REWORK_TABLE, log, _inSpo.getLotId());
			if(deleteIndex<1) {
				throw new LogicalException("SYSF000001");
			}
		}
		
	}

	@Override
	public void setSqlProxy() throws Exception {
		// TODO Auto-generated method stub
		hMGReworkSql.setInfo(this.userId, this.functionName, this.functionId, this.systemTimestamp);

	}

	@Override
	public void validateParameter() throws LogicalException {
		// TODO Auto-generated method stub

	}

	// setInput
	public void setInput(InputSpo in) {
		_inSpo = in;
	}

	// getOutput
	public OutputSpo getOutput() {
		return _outSpo;
	}

	// Input Service Parameter Object

	@Data // 使用data注解默认提供了getter,setter,以及toString方法
	public static class InputSpo {

		private String actionFlag;

		private String siteId;

		private String lotId;

		private String productName;

		private String productVer;

		private String stepName;

		private String qty;

		private String equipmentId;

		private String reworkReason;

		private String reworkCodeId;

		private String createUser;

		private String lmUser;

	}

	@Data
	public static class OutputSpo {
		
	}
}
