import javax.lang.model.util.SimpleElementVisitor6;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.lang.IndexOutOfBoundsException;

public class MeetingCalendar {
    // creating fields for MeetingCalendar class
    public HashMap<String, String> working_hours = new HashMap<>();
    public ArrayList<HashMap<String, String>> planned_meeting = new ArrayList<>();

    //creating constructor for MeetingCalendar class
    public MeetingCalendar (HashMap<String, String> working_hours, ArrayList<HashMap<String, String>> planned_meeting) {
        this.working_hours = working_hours;
        this.planned_meeting = planned_meeting;
    }


    public static ArrayList<String[]> letsMeet (MeetingCalendar calendar_1, MeetingCalendar calendar_2) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        int meeting_duration = 30;

        //lists with available hours for the two MeetingCalendar objects given as parameters (free hours including pre-planned meetings)
        ArrayList<Date> list_workingh_1 = new ArrayList<>();

        //including working hours from ... to and dividing the working day into 30 min periods
        String start_1 = calendar_1.working_hours.get("start");
        String end_1 = calendar_1.working_hours.get("end");

        Date start_1d = formatter.parse(start_1);
        Calendar start_1c = new GregorianCalendar();
        start_1c.setTime(start_1d);

        Date end_1d = formatter.parse(end_1);

        int time_in_job = (int) ((end_1d.getTime() - start_1d.getTime()) / 60_000) / meeting_duration;

        //creating a list with 30 minutes periods
        list_workingh_1.add(start_1c.getTime());
        for (int i = 1; i <= time_in_job; i++) {
            start_1c.add(Calendar.MINUTE, meeting_duration );
            list_workingh_1.add(start_1c.getTime());
        }

        //inclusion of the previously planned meetings
        //list with 30 minutes periods during which there are meetings
        ArrayList<Date> list_meeting_1 = new ArrayList<>();
        Date x_1 = new Date();

        for (HashMap<String, String> map : calendar_1.planned_meeting) {

            //checking the duration of the meeting
            String start = map.get("start");
            String end = map.get("end");

            Date start_d = formatter.parse(start);
            Date end_d = formatter.parse(end);

            int time_meeting = (int) ((end_d.getTime() - start_d.getTime()) / 60_000) / meeting_duration;

            //if meeting start at x time and next will start at x time
            if (start_d.equals(x_1)) {
                list_meeting_1.add(start_d);
            }

            x_1 = end_d;

            //list with 30 minutes periods during which there are meetings
            Calendar m_1 = new GregorianCalendar();
            m_1.setTime(start_d);
            for (int i = 1; i < time_meeting; i++) {
                m_1.add(Calendar.MINUTE, meeting_duration );
                list_meeting_1.add(m_1.getTime());
            }
        }

        //removal from the list 30 minutes period which have planned meetings
        for (int i = 0; i < list_workingh_1.size(); i++) {
            for(int j = 0; j < list_meeting_1.size(); j++) {
                if (list_workingh_1.get(i).equals(list_meeting_1.get(j))) {
                    list_workingh_1.remove(i);
                }
            }
        }

        //iteration to second MeetingCalendar given as a parameter
        ArrayList<Date> list_workingh_2 = new ArrayList<>();

        //including working hours from ... to and dividing the working day into 30 min periods
        String start_2 = calendar_2.working_hours.get("start");
        String end_2 = calendar_2.working_hours.get("end");

        Date start_2d = formatter.parse(start_2);
        Calendar start_2c = new GregorianCalendar();
        start_2c.setTime(start_2d);

        Date end_2d = formatter.parse(end_2);

        int time_in_job_2 = (int) ((end_2d.getTime() - start_2d.getTime()) / 60_000) / meeting_duration;

        //creating a list with 30 minutes periods
        list_workingh_2.add(start_2c.getTime());
        Calendar c_2 = start_2c;
        for (int i = 1; i <= time_in_job_2; i++) {
            c_2.add(Calendar.MINUTE, meeting_duration );
            list_workingh_2.add(c_2.getTime());
        }

        //inclusion of the previously planned meetings
        //list with 30 minutes periods during which there are meetings

        ArrayList<Date> list_meeting_2 = new ArrayList<>();
        Date x_2 = new Date();

        for (HashMap<String, String> map : calendar_2.planned_meeting) {

            //checking the duration of the meeting
            String start = map.get("start");
            String end = map.get("end");

            Date start_d = formatter.parse(start);
            Date end_d = formatter.parse(end);

            int time_meeting = (int) ((end_d.getTime() - start_d.getTime()) / 60_000) / meeting_duration;

            //if meeting start at x time and next will start at x time
            if (start_d.equals(x_2)) {
                list_meeting_2.add(start_d);
            }

            x_2 = end_d;

            //list with 30 minutes periods during which there are meetings
            Calendar m_2 = new GregorianCalendar();
            m_2.setTime(start_d);
            for (int i = 1; i < time_meeting; i++) {
                m_2.add(Calendar.MINUTE, meeting_duration );
                list_meeting_2.add(m_2.getTime());
            }
        }

        //removal from the list 30 minutes period which have planned meetings
        for (int i = 0; i < list_workingh_2.size(); i++) {
            for(int j = 0; j < list_meeting_2.size(); j++) {
                if (list_workingh_2.get(i).equals(list_meeting_2.get(j))) {
                    list_workingh_2.remove(i);
                }
            }
        }

        //creating a list with common periods of time for both MeetingCalendar object
        ArrayList<Date> common_time = new ArrayList<>();
        for (int i = 0; i < list_workingh_1.size(); i++) {
            for (int j = 0; j < list_workingh_2.size(); j++) {
                if (list_workingh_1.get(i).equals(list_workingh_2.get(j))) {
                    common_time.add(list_workingh_1.get(i));
                }
            }
        }

        //list of possible meetings
        ArrayList<String[]> optional_meetings = new ArrayList<>();

        for (int i = 0; i < common_time.size(); i++) {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();

            start.setTime(common_time.get(i));
            try {
                end.setTime(common_time.get(i + 1));
            }
            catch (IndexOutOfBoundsException e) {
                break;
            }

            Calendar duration = new GregorianCalendar();
            duration = start;
            duration.add(Calendar.MINUTE, meeting_duration);

            if (duration.equals(end)) {
                optional_meetings.add(new String[] {formatter.format(common_time.get(i)), formatter.format(common_time.get(i + 1))});
            }
        }

        //printing possible meetings on the screen
        for (int i = 0; i < optional_meetings.size(); i++) {
            System.out.println("[" + optional_meetings.get(i)[0] + ", " + optional_meetings.get(i)[1] + "]");
        }
        return optional_meetings;
    }


    public static void main(String[] args) {
        HashMap<String, String> working_hours_1 = new HashMap<>();

        working_hours_1.put("start", "09:00");
        working_hours_1.put("end", "20:00");

        ArrayList<HashMap<String, String>> planned_meeting_1 = new ArrayList<>();

        HashMap<String, String> meeting_1_1 = new HashMap<>();
        meeting_1_1.put("start", "09:00");
        meeting_1_1.put("end", "10:30");

        HashMap<String, String> meeting_1_2 = new HashMap<>();
        meeting_1_2.put("start", "12:00");
        meeting_1_2.put("end", "13:00");

        HashMap<String, String> meeting_1_3 = new HashMap<>();
        meeting_1_3.put("start", "16:00");
        meeting_1_3.put("end", "18:30");

        planned_meeting_1.add(meeting_1_1);
        planned_meeting_1.add(meeting_1_2);
        planned_meeting_1.add(meeting_1_3);

        HashMap<String, String> working_hours_2 = new HashMap<>();

        working_hours_2.put("start", "10:00");
        working_hours_2.put("end", "18:30");

        ArrayList<HashMap<String, String>> planned_meeting_2 = new ArrayList<>();

        HashMap<String, String> meeting_2_1 = new HashMap<>();
        meeting_2_1.put("start", "10:00");
        meeting_2_1.put("end", "11:30");

        HashMap<String, String> meeting_2_2 = new HashMap<>();
        meeting_2_2.put("start", "12:30");
        meeting_2_2.put("end", "14:30");

        HashMap<String, String> meeting_2_3 = new HashMap<>();
        meeting_2_3.put("start", "14:30");
        meeting_2_3.put("end", "15:00");

        HashMap<String, String> meeting_2_4 = new HashMap<>();
        meeting_2_4.put("start", "16:00");
        meeting_2_4.put("end", "17:00");

        planned_meeting_2.add(meeting_2_1);
        planned_meeting_2.add(meeting_2_2);
        planned_meeting_2.add(meeting_2_3);
        planned_meeting_2.add(meeting_2_4);


        MeetingCalendar calendar_1 = new MeetingCalendar(working_hours_1, planned_meeting_1);
        MeetingCalendar calendar_2 = new MeetingCalendar(working_hours_2, planned_meeting_2);

        try {
            MeetingCalendar.letsMeet(calendar_1, calendar_2);
        }
        catch (ParseException e) {
            System.out.println(e);
        }
    }
}




