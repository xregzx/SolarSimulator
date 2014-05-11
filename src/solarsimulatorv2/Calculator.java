/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package solarsimulatorv2;

/**
 *
 * @author xregzx
 */
public class Calculator {
    //public final int Cn = 1;
    //public final double RHO = 0.2;
    //returns the solar declination angle in degrees
    public double solarDeclination(int n){
        double angle, out;
        
        angle = (360*(284+n))/365;
        out = 23.45*Math.sin(Math.toRadians(angle));
        return out;
    }
    //returns B in degrees
    public double b(int n){
        return (360*(n-81))/364;
    }
    //returns Solar Time in MINUTES
    public double solarTime(int lst,double et,double standardMeridian,double longitude){
        int lstMin = lst*60;
        
        return (lstMin+et+4*(standardMeridian-longitude));
    }
    //returns time equation
    public double timeEquation(double b){
        return 9.87*Math.sin(Math.toRadians(2*b))-7.53*Math.cos(Math.toRadians(b))-1.5*Math.sin(Math.toRadians(b));
        
    }
    //returns hour angle
    public double hourAngle(double solarTimeInMinutes){
        return (solarTimeInMinutes-720)/4;
    }
    //returns alpha in degrees
    public double alpha(double latitude,double sDec,double hAngle){
        double temp =  Math.asin(Math.cos(Math.toRadians(latitude))*Math.cos(Math.toRadians(sDec))*Math.cos(Math.toRadians(hAngle))+Math.sin(Math.toRadians(latitude))*Math.sin(Math.toRadians(sDec)));
        return Math.toDegrees(temp);
    }
    //returns the solar zenith angle
    public double solarZenith(double alpha){
        return (90-alpha);
    }
    //returns alpha(s)
    public double alphaS(double sDec,double hAngle,double alpha){
        double temp = Math.asin((Math.cos(Math.toRadians(sDec))*Math.sin(Math.toRadians(hAngle)))/Math.cos(Math.toRadians(alpha)));
        return Math.toDegrees(temp);
    }
    //return sunrise/sunset in MINUTES
    public double hssNhsr(double latitude,double sDec){
        double temp = Math.acos(-Math.tan(Math.toRadians(latitude))*Math.tan(Math.toRadians(sDec)));
        return (Math.toDegrees(temp))*4;
        
    }
    //returns the extraterrestrial solar radiation
    public double i(int n){
        return (1353*(1+0.034*Math.cos(Math.toRadians((360*n)/365.25))));
    }
    //returns sunrise(Solar time) - MINUTES
    public double sunriseSolarTime(double hsr){
        return (720-hsr);
    }
    //returns sunset(Solar time) - MINUTES
    public double sunsetSolarTime(double hss){
        return hss;
    }
    //returns sunset or sunrise(Local Time) - MINUTES
    public double sunriseOrSunset(double solarTime,double et,double standardMeridian,double longitude ){
        return (solarTime-et-4*(standardMeridian-longitude));
    }
    //returns Ibn
    public double ibn(double cn,double i,double k,double alpha){
        double exponent = -k/Math.sin(Math.toRadians(alpha));
        double temp = (cn*i*Math.exp(exponent));
        if(temp>1353){
            return 0.0;
        }else{
            return temp;
        }
    }
    //return beam/direct radiation
    public double ibc(double ibn,double cosI){
        return (ibn*cosI);
    }
    //return cos(i)
    public double cosI(double alpha,double alphaS,double panelAzimuth,double tilt){
        return Math.cos(Math.toRadians(alpha))*Math.cos(Math.toRadians(alphaS-panelAzimuth))*Math.sin(Math.toRadians(tilt))+Math.sin(Math.toRadians(alpha))*Math.cos(Math.toRadians(tilt));
    }
    //return sky diffuse radiation
    public double idc(double c,double ibn,double tilt){
        return c*ibn*Math.pow(Math.cos(Math.toRadians(tilt/2)), 2);
    }
    //returns ground reflected radiation
    public double irc(double rho,double ibn,double alpha,double c,double tilt){
        return (rho*ibn*(Math.sin(Math.toRadians(alpha))+c)*Math.pow(Math.sin(Math.toRadians(tilt/2)),2));
    }
    //return N
    public int n(int month,int date){
        int out = 0;
        int n[] = {31,29,31,30,31,30,31,31,30,31,30,31}; 
        for(int i=0;i<month;i++){
            out+=n[i];
        }
        out += date;
        return out;
    }
    public double k(int month){
        double values[] = {0.142, 0.144, 0.156, 0.180, 0.196, 0.205, 0.207, 0.201, 0.177, 0.160, 0.149, 0.142};
        double temp = 0.0;
        for(int i=0;i<12;i++){
            if(month==i){
                temp = values[i];
                break;
            }
        }
        return temp;
    }
    public double c(int month){
        double values[] = {0.058, 0.060, 0.071, 0.097, 0.121, 0.134, 0.136, 0.122, 0.092, 0.073, 0.063, 0.057};
        double temp = 0.0;
        for(int i=0;i<12;i++){
            if(month==i){
                temp = values[i];
                break;
            }
        }
        return temp;
    }
    public String sunrise(int month,int date,double lat,double standardMeridian,double longitude){
        int n = n(month,date);
        double sDec = solarDeclination(n);
        double b = b(n);
        double et = timeEquation(b);
        double hsr = hssNhsr(lat,sDec);// hsr is in MINUTES
        double solarSunrise = 720-hsr;
        double sunrise = solarSunrise-et-4*(standardMeridian-longitude);
        int outHours = (int)(sunrise/60);
        int outMinutes = (int)(((sunrise/60)-outHours)*60);
        String out = outHours+":"+outMinutes+" AM";
        
        return out;
        
    }
    public String sunset(int month,int date,double lat,double standardMeridian,double longitude){
        int n = n(month,date);
        double sDec = solarDeclination(n);
        double b = b(n);
        double et = timeEquation(b);
        double hss = hssNhsr(lat,sDec);// hss is in MINUTES
        double solarSunset = hss;
        double sunset = solarSunset-et-4*(standardMeridian-longitude);
        int outHours = (int)(sunset/60);
        int outMinutes = (int)(((sunset/60)-outHours)*60);
        String out = outHours+":"+outMinutes+" PM";
        
        return out;
        
    }
    
    //returns the instantaneous solar radiation using the above methods
    public double insSolarRadiation(int lst,double lat,double longitude,double standardMeridian,double tilt,double azimuth,int month,int date,double cn,double rho){
        int n = n(month,date);
        double sDec = solarDeclination(n);
        double b = b(n);
        double eqTime = timeEquation(b);
        double solarTime = solarTime(lst,eqTime,standardMeridian,longitude);
        double hourAngle = hourAngle(solarTime);
        double alpha = alpha(lat,sDec,hourAngle);
        double alphaS = alphaS(sDec, hourAngle,alpha);
        double cosI = cosI(alpha,alphaS,azimuth,tilt);
        double i = i(n);
        double c = c(month);
        double k = k(month);
        double ibn = ibn(cn,i,k,alpha);
        double ibc = ibc(ibn,cosI);
        double idc = idc(c,ibn,tilt);
        double irc = irc(rho,ibn,alpha,c,tilt);
        
        return (ibc+idc+irc);
    }
    public double totalRad(double sm,double longi,double lat,double tilt,double azimuth,double cn,double rho,int month,int date){
        double out = 0.0;
        for(int i=6;i<19;i++){
            out+=insSolarRadiation(i, lat, longi, sm, tilt, azimuth, month, date, cn, rho);
        }
        return out;
    }
    public double pRatedArraySize(int eConsumed,double totRad,double eff){
        return (eConsumed*1000)/(totRad*eff);
    }
    public double numModules(double pRatedArraySize,double pRatedInput){
        return  pRatedArraySize/pRatedInput;
    }
    public double pNew(double numModules,double pRatedInput){
        return Math.ceil(numModules)*pRatedInput;
    }
    //Energy produced per day
    public double eProducedPerDay(int eConsumed,double totRad,double eff,double pRatedInput){
        double pRAS = pRatedArraySize(eConsumed,totRad,eff);
        double nMod = numModules(pRAS,pRatedInput);
        double pN = pNew(nMod,pRatedInput);
        
        return (pN*totRad*eff)/1000;
    }
    
    public double eProducedPerMonth(double eProducedPerDay){
        return eProducedPerDay*31;
    }
    public double eProducedPerYear(double eProducedPerDay){
        return eProducedPerDay*365;
    }
    //Number of Panels required
    public double numPanels(int eConsumed,double totRad,double eff,double pRatedInput){
        double pRAS = pRatedArraySize(eConsumed,totRad,eff);
        double nMod = numModules(pRAS,pRatedInput);
        
        return Math.ceil(nMod);
    }
    //Number of Panels in series
    public int numPanelSeries(int vBatt,int vPannel){
        return (int)(vBatt/vPannel);
    }
    //Number of series string in parallel
    public double numStringParallel(double numPanels,int numPanelSeries){
        return numPanels/numPanelSeries;
    }
    
    
    
    
}
