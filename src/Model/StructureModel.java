package Model;

import Controllers.jobInfoController;
import Controllers.setupSheetsController;

public class StructureModel {

    public jobInfoController jobInfo;
    public setupSheetsController setupSheets = new setupSheetsController();

    public StructureModel(jobInfoController jobInfo) {
        this.jobInfo = jobInfo;
    }

    public StructureModel(setupSheetsController setupSheets) {
        this.setupSheets = setupSheets;
    }

}
