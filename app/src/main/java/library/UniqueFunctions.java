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

    public String getFormattedDate(String date) {

        String day = date.substring(3, 5);
        String month = date.substring(0, 2);

        String final_date = MonthIntToString(month) + " " + day;

        return final_date;

    }

    public String getFullGender(String gender) {

        if(gender.equals("m"))
            return "Male";
        else
            return "Female";

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

    public String getShortYear(String year_long) {
        if(year_long.length() == 1)
            return year_long;
        switch (year_long) {
            case "First Year":
                return "1";
            case "Second Year":
                return "2";
            case "Third Year":
                return "3";
            case "Fourth Year":
                return "4";
            case "Faculty":
                return "5";
            case "All":
                return "12345";
            default:
                return "6";
        }
    }

    public String getNumberWithSubscript(String num) {

        switch (num) {
            case "1":
                return "1st";
            case "2":
                return "2nd";
            case "3":
                return "3rd";
            case "4":
                return "4th";
            default:
                return num + "th";
        }
    }

}
