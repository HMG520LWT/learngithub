package kyber.wip_rework_station_control.Info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.exception.LogicalException;
import com.kyber.core.runtime.LogicalService;
import com.kyber.core.sql.KyberSqlProxy;
import kyber.wip_rework_station.sql.action.ReworkTrxSqlAction;
import lombok.Data;

@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ModifyReworkInfo extends LogicalService {

	private String ADD_FLAG = "ADD";
	private String UPDATE_FLAG = "UPDATE";
	private String DELETE_FLAG="DELETE";

	private InputSpo _inSpo;
	private OutputSpo _outSpo = new OutputSpo();

	
	@Autowired
	@Qualifier("reworkTrxSql")
	private KyberSqlProxy reworkTrxSql;
 
	@Override
	public void execute() throws Exception {
		


		//如果动作是ADD的话
		if(ADD_FLAG.equals(_inSpo.actionFlag)) {
			int insertIndex = reworkTrxSql.executeUpdate(ReworkTrxSqlAction.INSERT_REWORK_TABLE, log,
					_inSpo.getSiteId(),_inSpo.getLotId(),
					_inSpo.getProductName(),_inSpo.getProductVer(),_inSpo.getStepName(),_inSpo.getQty(),
					_inSpo.getEquipmentId(),_inSpo.getReworkReason(),_inSpo.getReworkCodeId(),_inSpo.getCreateUser(),_inSpo.getLmUser());
			if(insertIndex<1) {
				throw new LogicalException("inset error"); // 添加失败
			}
		}
		
		if(UPDATE_FLAG.equals(_inSpo.actionFlag)) {
			int updateIndex = reworkTrxSql.executeUpdate(ReworkTrxSqlAction.UPDATE_REWORK_TABLE, log,_inSpo.getQty(),_inSpo.getLotId());
			if(updateIndex<1) {
				throw new LogicalException("SYSF000001"); // 更新失败
			}
		}
		
		
		if(DELETE_FLAG.equals(_inSpo.actionFlag)) {
			int deleteIndex = reworkTrxSql.executeUpdate(ReworkTrxSqlAction.DELETE_REWORK_TABLE, log,_inSpo.getLotId());
			
			// 判断结果是否成功刪除
			if(deleteIndex<1) {
				throw new LogicalException("SYSF000001");
			}
		}
	}

	@Override
	public void setSqlProxy() throws Exception {
		// TODO Auto-generated method stub
		reworkTrxSql.setInfo(this.userId, this.functionName, this.functionId, this.systemTimestamp);
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

	
	//Input Service Parameter Object
	
	@Data //使用data注解默认提供了getter,setter,以及toString方法
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
 
        private	String reworkCodeId;
		
		private String createUser;

        private String lmUser;
	}

	
	
	 // Output Service Parameter Object
	public static class OutputSpo {

	}
}
