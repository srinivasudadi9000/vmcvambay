package m.srinivas.vmcvambay;

/**
 * Created by USER on 17-07-2017.
 */

public class Drilldown {
    String RegNo,intRegid,Areaname,Sector,PlotNumber,OriginalName,CurrentName;
    Drilldown(String RegNo,String intRegid,String Areaname,String Sector,String PlotNumber,String OriginalName,String CurrentName){
         this.RegNo = RegNo;this.intRegid=intRegid;this.Areaname= Areaname;this.Sector= Sector;
         this.PlotNumber = PlotNumber;this.OriginalName= OriginalName;this.CurrentName=CurrentName;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getIntRegid() {
        return intRegid;
    }

    public void setIntRegid(String intRegid) {
        this.intRegid = intRegid;
    }

    public String getAreaname() {
        return Areaname;
    }

    public void setAreaname(String areaname) {
        Areaname = areaname;
    }

    public String getSector() {
        return Sector;
    }

    public void setSector(String sector) {
        Sector = sector;
    }

    public String getPlotNumber() {
        return PlotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        PlotNumber = plotNumber;
    }

    public String getOriginalName() {
        return OriginalName;
    }

    public void setOriginalName(String originalName) {
        OriginalName = originalName;
    }

    public String getCurrentName() {
        return CurrentName;
    }

    public void setCurrentName(String currentName) {
        CurrentName = currentName;
    }
}
