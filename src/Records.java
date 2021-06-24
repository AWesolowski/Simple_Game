import java.util.ArrayList;

public class Records
{
    private ArrayList<String> nicknames;
    private ArrayList<Double> points;
    private int number_of_records = 0;

    public Records()
    {
        nicknames = new ArrayList<>();
        points = new ArrayList<>();
    }


    public String getNickname(int index)
    {
        return nicknames.get(index);
    }


    public Double getPoints(int index)
    {
        return points.get(index);
    }


    public void addValues(String given_nickname, Double given_points)
    {
        nicknames.add(given_nickname);
        points.add(given_points);
        number_of_records++;
    }


    public void addValues(String given_nickname, Double given_points, int index)
    {
        nicknames.add(index, given_nickname);
        points.add(index, given_points);
        number_of_records++;
    }


    public int getNumber_of_records()
    {
        return number_of_records;
    }
}
