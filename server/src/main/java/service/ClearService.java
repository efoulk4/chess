package service;

import dataaccess.DataAccess;

public class ClearService {
    DataAccess dataAccess;
    public ClearService(DataAccess dataAccess){this.dataAccess =dataAccess;}
    public void Clear(){
        dataAccess.clear();
    }
}
