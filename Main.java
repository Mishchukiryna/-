import com.sun.security.jgss.GSSUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        LinkedList<City> cities = new LinkedList<>();
        cities.add(new City("Kyiv", 50.4501, 30.5234, ZoneId.of("GMT+3")));
        cities.add(new City("Rome", 41.9028, 12.4964, ZoneId.of("GMT+2")));
        cities.add(new City("London", 51.5074, -0.1278, ZoneId.of("GMT+1")));
        cities.add(new City("New York", 40.7128, -74.0060, ZoneId.of("GMT-4")));
        cities.add(new City("Istanbul", 41.0082, 28.9784, ZoneId.of("GMT+3")));
        cities.add(new City("San Francisco", 37.7749, -122.4194, ZoneId.of("GMT-7")));
        cities.add(new City("Melbourne", -37.8136, 144.9631, ZoneId.of("GMT+11")));
        cities.add(new City("Singapore", 1.3521, 103.8198, ZoneId.of("GMT+8")));
        cities.add(new City("Tokyo", 35.6762, 139.6503, ZoneId.of("GMT+9")));
        cities.add(new City("Sydney", -33.8688, 151.2093, ZoneId.of("GMT+11")));
        cities.add(new City("Paris", 48.8566, 2.3522, ZoneId.of("GMT+2")));
        cities.add(new City("Berlin", 52.5200, 13.4050, ZoneId.of("GMT+2")));
        cities.add(new City("Dubai", 25.2048, 55.2708, ZoneId.of("GMT+4")));
        cities.add(new City("Hong Kong", 22.3193, 114.1694, ZoneId.of("GMT+8")));
        cities.add(new City("Rio de Janeiro", -22.9068, -43.1729, ZoneId.of("GMT-3")));
        cities.add(new City("Toronto", 43.651070, -79.347015, ZoneId.of("GMT-4")));
        cities.add(new City("Los Angeles", 34.0522, -118.2437, ZoneId.of("GMT-7")));
        cities.add(new City("Warsaw", 52.2297, 21.0122, ZoneId.of("GMT+2")));
        cities.add(new City("Seoul", 37.5665, 126.9780, ZoneId.of("GMT+9")));

        LinkedList<TravelMeans> means = new LinkedList<>();
        means.add(new TravelMeans("Car", 20, 150));
        means.add(new TravelMeans("Train", 10, 500));
        means.add(new TravelMeans("Plane", 100, 2000));

        while(true){
            System.out.println("What would you like to do?");
            System.out.println(" 1. Open the travel planner app");
            System.out.println(" 2. Exit");
            int input = 0;
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextInt();
            if(input < 1 || input > 2)
                System.out.println("Please enter a number between 1 and 2");
            else{
                if(input == 1){
                    System.out.println("Available cities:");
                    LinkedList<City> chosencities = new LinkedList<>();
                    Scanner cityScanner = new Scanner(System.in);
                    for(City city : cities){
                        System.out.println(" -" + city.toString());
                    }
                    System.out.println("Choose the city of departure:");
                    while(true){
                        String cityName = cityScanner.nextLine();
                        boolean cityFound = false;
                        for(City c: cities){
                            if (c.getName().equalsIgnoreCase(cityName)) {
                                chosencities.add(c);
                                cityFound = true;
                                break;
                            }
                        }
                        if(cityFound) break;
                        else System.out.println("Please enter a valid city name");
                    }
                    while(true){
                        System.out.println("Enter next destination city (or '-' to finish): ");
                        boolean isContinuePressed = false;
                        while(true){
                            String cityName = cityScanner.nextLine();
                            if(Objects.equals(cityName, "-")){
                                isContinuePressed = true;
                                break;
                            }
                            boolean cityFound = false;
                            for(City c: cities){
                                if (c.getName().equalsIgnoreCase(cityName)) { 
                                    chosencities.add(c);
                                    cityFound = true;
                                    break;
                                }
                            }
                            if(cityFound) break;
                            else System.out.println("Please enter a valid city name");
                        }
                        if(isContinuePressed) break;
                    }

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime dateTime = null;
                    boolean validInput = false;
                    System.out.println("Enter departure date and time (yyyy-MM-dd HH:mm):");
                    while (!validInput) {
                        String timeInput = scanner.nextLine();
                        timeInput = scanner.nextLine();
                        try {
                            dateTime = LocalDateTime.parse(timeInput, formatter);
                            validInput = true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid date format. Please use yyyy-MM-dd HH:mm");
                        }
                    }
                    System.out.println("Are you time limited? (y/n): ");
                    boolean tooLowOnTime = false;
                    while(true){
                        String response = scanner.nextLine();
                        if(Objects.equals(response, "n")){
                            System.out.println("Alright, let's continue then!");
                            break;
                        }
                        else if(Objects.equals(response, "y")){
                            System.out.println("Alright, how much time do you have (in hours)?");
                            int timeInput = scanner.nextInt();
                            double fullDistance = 0.0;
                            for(int i = 1; i < chosencities.size(); i++)
                                fullDistance += calculateDistance(chosencities.get(i - 1), chosencities.get(i));
                            if(fullDistance / timeInput > 2000.0){
                                System.out.println("Sorry, the fastest we can do is 2000km/h and it's going to be " + (int) fullDistance/2000 + " hours");
                                tooLowOnTime = true;
                                break;
                            }
                            else{
                                System.out.println("Alright, you're going to be travelling by plane at the speed of "
                                        + (int)fullDistance/timeInput + " km/h. Have a nice trip!");
                                OutputTravel(chosencities, dateTime, formatter, (int)fullDistance/timeInput);
                                tooLowOnTime = true;
                                break;
                            }
                        }
                        else System.out.println("Enter 'y' or 'n' for yes or no.");
                    }
                    if(tooLowOnTime){
                        continue;
                    }
                    System.out.println("Available travel means: ");
                    for(TravelMeans t: means){
                        System.out.println(t.toString());
                    }
                    TravelMeans chosenmeans = null;
                    while(true){
                        String meansName = cityScanner.nextLine();
                        boolean meansFound = false;
                        for(TravelMeans t: means){
                            if (t.getName().equalsIgnoreCase(meansName)) {
                                chosenmeans = t;
                                meansFound = true;
                                break;
                            }
                        }
                        if(meansFound) break;
                        else System.out.println("Please enter a valid travel means name");
                    }

                    System.out.println("Enter speed you'd like to go at: " + chosenmeans.getMinSpeed()+ "km/h" + " - " + chosenmeans.getMaxSpeed() + "km/h");
                    int speed = 0;
                    while(true){
                        speed = scanner.nextInt();
                        if(speed < chosenmeans.getMinSpeed() || speed > chosenmeans.getMaxSpeed()){
                            System.out.println("Enter the speed between " + chosenmeans.getMinSpeed() + "km/h" + " - " + chosenmeans.getMaxSpeed() + "km/h");
                            continue;
                        }
                        break;
                    }

                    OutputTravel(chosencities, dateTime, formatter, speed);
                }
                if(input == 2){
                    System.out.println("Exiting the program. Goodbye!");
                    break;
                }
            }
        }
    }

    public static void OutputTravel(LinkedList<City> chosencities, LocalDateTime dateTime, DateTimeFormatter formatter, int speed) {
        double totalTravelTime = 0.0;
        double totalDistance = 0.0;
        ZonedDateTime zonedTime = dateTime.atZone(chosencities.get(0).getTimeZone());
        
        System.out.println("\n*** Travel Itinerary ***\n");
        
        for (int i = 1; i < chosencities.size(); i++) {
            System.out.println("Leg " + i + ": From " + chosencities.get(i - 1).getName() + " to " + chosencities.get(i).getName());
            System.out.println("------------------------------------------------------");
            System.out.println("Departure (" + chosencities.get(i - 1).getName() + " time): " + zonedTime.format(formatter));
    
            double timeTaken = calculateTime(chosencities.get(i - 1), chosencities.get(i), speed);
            double distanceTravelled = calculateDistance(chosencities.get(i - 1), chosencities.get(i));
    
            int hours = (int) timeTaken;
            int minutes = (int) ((timeTaken - hours) * 60);
            zonedTime = zonedTime.plusHours(hours).plusMinutes(minutes);
            zonedTime = zonedTime.withZoneSameInstant(chosencities.get(i).getTimeZone());
    
            System.out.println("Arrival (" + chosencities.get(i).getName() + " time): " + zonedTime.format(formatter));
            System.out.println("Time taken: " + hours + " hours and " + minutes + " minutes");
            System.out.println("Distance travelled: " + String.format("%.2f", distanceTravelled) + " km.");
            System.out.println();
    
            totalTravelTime += timeTaken;
            totalDistance += distanceTravelled;
        }
    
        int totalHours = (int) totalTravelTime;
        int totalMinutes = (int) ((totalTravelTime - (double) totalHours) * 60);
        
        System.out.println("======================================================");
        System.out.println("Total time for the entire trip: " + totalHours + " hours and " + totalMinutes + " minutes");
        System.out.println("Total distance travelled: " + String.format("%.2f", totalDistance) + " km.");
        System.out.println("======================================================");
    }
    

    //формула гаверсинуса
    public static double calculateDistance(City city1, City city2) {
        final int EARTH_RADIUS = 6378;

        double latDistance = Math.toRadians(city2.getLatitude() - city1.getLatitude());
        double lonDistance = Math.toRadians(city2.getLongitude() - city1.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(city1.getLatitude())) * Math.cos(Math.toRadians(city2.getLatitude())) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    // швидкість - км/год, результат - в годинах
    public static double calculateTime(City city1, City city2, int speed) {
        double distance = calculateDistance(city1, city2);
        return distance / (double)speed;
    }
}

