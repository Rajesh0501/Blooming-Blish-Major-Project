import java.util.*;

public class AaravAndArjun {

    // Function to calculate the area of a polygon using Shoelace formula
    public static double calculateArea(List<int[]> polygonPoints) {
        double area = 0;
        int n = polygonPoints.size();
        for (int i = 0; i < n; i++) {
            int[] p1 = polygonPoints.get(i);
            int[] p2 = polygonPoints.get((i + 1) % n); // Next point, with wrap-around
            area += p1[0] * p2[1] - p2[0] * p1[1];
        }
        return Math.abs(area) / 2.0;
    }

    // Function to calculate the length of a line segment
    public static double calculateLength(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Function to check if the polygon can be formed
    public static boolean isValidPolygon(List<int[]> sticks) {
        // Track endpoints and check if they form a continuous path
        Map<String, Integer> pointCount = new HashMap<>();
        for (int[] stick : sticks) {
            String startPoint = stick[0] + "," + stick[1];
            String endPoint = stick[2] + "," + stick[3];
            pointCount.put(startPoint, pointCount.getOrDefault(startPoint, 0) + 1);
            pointCount.put(endPoint, pointCount.getOrDefault(endPoint, 0) + 1);
        }

        // A valid polygon must have exactly 2 occurrences for each point (since every point is used twice)
        for (int count : pointCount.values()) {
            if (count != 2) {
                return false; // Invalid if any point doesn't occur exactly twice
            }
        }
        return true;
    }

    // Function to check if the leftover sticks can form the same shape and size
    public static boolean canFormSameShape(List<int[]> sticks, double polygonPerimeter) {
        double leftoverLength = 0;
        for (int[] stick : sticks) {
            leftoverLength += calculateLength(stick[0], stick[1], stick[2], stick[3]);
        }
        return Math.abs(leftoverLength - polygonPerimeter) < 0.01;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        // Store coordinates and sticks
        List<int[]> sticks = new ArrayList<>();
        double totalStickLength = 0;

        // Read the input sticks
        for (int i = 0; i < N; i++) {
            int x1 = sc.nextInt();
            int y1 = sc.nextInt();
            int x2 = sc.nextInt();
            int y2 = sc.nextInt();

            // Add stick information
            sticks.add(new int[]{x1, y1, x2, y2});

            // Calculate total length of all sticks
            totalStickLength += calculateLength(x1, y1, x2, y2);
        }

        // Checking for valid polygon
        if (!isValidPolygon(sticks)) {
            System.out.println("No");
            return;
        }

        // Create the polygon points (closed figure)
        List<int[]> polygonPoints = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        int[] start = sticks.get(0);
        polygonPoints.add(new int[]{start[0], start[1]});
        visited.add(start[0] + "," + start[1]);

        // Follow the sticks to find the closed polygon
        for (int i = 0; i < N; i++) {
            int[] stick = sticks.get(i);
            if (visited.contains(stick[2] + "," + stick[3])) {
                polygonPoints.add(new int[]{stick[0], stick[1]});
                visited.add(stick[0] + "," + stick[1]);
            }
        }

        // Calculate the area of the closed polygon
        double area = calculateArea(polygonPoints);
        System.out.println("Yes");

        // Calculate the closed polygon's perimeter
        double closedPolygonLength = 0;
        for (int i = 0; i < polygonPoints.size(); i++) {
            int[] p1 = polygonPoints.get(i);
            int[] p2 = polygonPoints.get((i + 1) % polygonPoints.size());
            closedPolygonLength += calculateLength(p1[0], p1[1], p2[0], p2[1]);
        }

        // Check if the leftover sticks can form the same shape and size
        if (canFormSameShape(sticks, closedPolygonLength)) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }

        // Print area with 2 decimal places
        System.out.printf("%.2f\n", area);
    }
}
