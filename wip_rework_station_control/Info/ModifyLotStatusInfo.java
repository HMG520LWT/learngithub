package kyber.wip_rework_station_control.Info;

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
import lombok.Data;

@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ModifyLotStatusInfo extends LogicalService {

	    // �����������
		private InputSpo _inSpo;
	    private OutputSpo _outSpo = new OutputSpo();
	
	    // ����dao��
	    @Autowired
		@Qualifier("lotStatusTrxSql")
		private KyberSqlProxy lotStatusTrxSql;

	
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		
		// �����ȡ���Ĳ��������߼��ж�
		/*
		 * if(CheckUtil.isNull(_inSpo.getSiteId())) { throw new
		 * LogicalException("E000016"); }
		 */
        
        if(CheckUtil.isNull(_inSpo.getLotId())) {
			throw new LogicalException("E000003");// Lot ID is null.
		}
        
        //ִ�н��
		int updateIndex = lotStatusTrxSql.executeUpdate(LotTrxSqlAction.UPDATE_LOT_STATUS, log,_inSpo.getLotId());

        
        if(updateIndex<1) {
        	throw new LogicalException("SYSF000001"); // update is error
        }
        
	}

	@Override
	public void setSqlProxy() throws Exception {
		// TODO Auto-generated method stub
		lotStatusTrxSql.setInfo(this.userId, this.functionName, this.functionId, this.systemTimestamp);
	}

	@Override
	public void validateParameter() throws LogicalException {
		// TODO Auto-generated method stub
		if(CheckUtil.isNull(_inSpo.getLotId())) {
			throw new LogicalException("E000003");// Lot ID is null.
		}
	}
	
	/**
	 * set input spo
	 */
	public void setInput(InputSpo in) {
		_inSpo = in;
	}

	/**
	 * get output spo
	 */
	public OutputSpo getOutput() {
		return _outSpo;
	}

	/**
	 * Input Service Parameter Object
	 */
	@Data
	public static class InputSpo {

		private String siteId;

		private String lotId;

	}

	/*
	 * Output Service Parameter Object
	 */
	public static class OutputSpo {

	}

}
