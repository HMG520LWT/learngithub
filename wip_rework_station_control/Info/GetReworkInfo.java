package kyber.wip_rework_station_control.Info;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import com.kyber.core.annotation.KyberComponent;
import com.kyber.core.annotation.KyberListColumn;
import com.kyber.core.exception.LogicalException;
import com.kyber.core.runtime.LogicalService;
import com.kyber.core.sql.KyberSqlProxy;
import com.kyber.core.util.CheckUtil;
import kyber.wip_rework_station.sql.action.ReworkQuerySqlAction;
import kyber.wip_rework_station_control.sql.oracle.ReworkQuerySql;
import lombok.Getter;
import lombok.Setter;

@KyberComponent
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetReworkInfo extends LogicalService {

   // 设置输入输出
	private InputSpo _inSpo;
    private OutputSpo _outSpo = new OutputSpo();
    
    //导入我们的持久层类
        @Autowired
 		@Qualifier("reworkQuerySql")
 		private KyberSqlProxy reworkQuerySql;

        // 我们执行的主要方法
	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		log.debug("ReworkQuery :", _inSpo.toString());

		List<Map> resultList = reworkQuerySql.executeQuery(ReworkQuerySqlAction.GET_REWORK_TABLE, log, _inSpo.getSiteId(),_inSpo.getLotId(),
				_inSpo.getReworkCodeId(),_inSpo.getStepName());
		
		// 判断siteID是否为空
		
		  if(CheckUtil.isNull(_inSpo.getSiteId())) { throw new
		  LogicalException("E000016"); // site is null
		  }
		 
        _outSpo.setResultList(resultList);

	}

	// 事务运行方法
	@Override
	public void setSqlProxy() throws Exception {
		// TODO Auto-generated method stub
		reworkQuerySql.setInfo(this.userId, this.functionName, this.functionId, this.systemTimestamp);
	}

	@Override
	public void validateParameter() throws LogicalException {
		// TODO Auto-generated method stub
		
	}

	// 输入构造方法
    public void setInput(InputSpo in)
    {
        _inSpo = in;        
    }
    

// 输出构造方法
    public OutputSpo getOutput()
    {
        return _outSpo;
    }
    
    //输入
    public static class InputSpo{
    	@Getter
    	@Setter
    	private String lotId;
    	
    	@Getter
    	@Setter
    	private String siteId;
    	
    	@Getter
		@Setter
		String stepName;
		
		@Getter
		@Setter
		String reworkCodeId;
	}
    
    // 输出
    public static class OutputSpo
    {
		@Getter
		@Setter
		@KyberListColumn(Column="",RequisiteColumn="")
		private List<Map> resultList;
    }

    
    
}
