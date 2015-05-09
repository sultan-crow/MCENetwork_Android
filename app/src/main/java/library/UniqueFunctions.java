package library;

public class UniqueFunctions {

    public String getFormattedDateTime(String date, String time) {

        String month = date.substring(5, 7);
        String day = date.substring(8);

        String hour = time.substring(0, 2);
        String min = time.substring(3, 5);

        int hour_int = Integer.parseInt(hour);

        String final_date = MonthIntToString(month) + " " + day;
        String final_time = convert24to12(hour_int) + ":" + min + " " + checkAMorPM(hour_int);

        return final_date + ", " + final_time;

    }

    public String MonthIntToString(String month) {
        switch(month) {
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "NA";
        }
    }

    public String convert24to12(int time) {
        if(time > 12)
            return String.valueOf(time - 12);
        else
            return String.valueOf(time);
    }

    public String checkAMorPM(int time) {
        if(time >= 12)
            return "PM";
        else
            return "AM";
    }

}
