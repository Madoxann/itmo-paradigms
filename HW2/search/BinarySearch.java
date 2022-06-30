package search;

public class BinarySearch {
    // P: key - integer, array isn't null, for all a[i] >= a[i+1], l < r && a[l] > key && a[r] <= key
    // Q: arr[r] <= key, r - min
    public static int searchRecursive(int[] arr, int l, int r, int key) {
        // P
        if (r - l > 1) {
            // P && r - l > 1
            int mid = (l + r) / 2;
            if (arr[mid] > key) {
                l = mid;
                // P && r - mid > 1
                // P && r - l/2 - r/2 > 1 -> P && r/2 - l/2 > 1
            } else {
                r = mid;
                // P && mid - l > 1
                // P && l/2 + r/2 - l > 1 -> r/2 - l/2 > 1
            }
            // P && r/2 - l/2 > 1 -> area halved
            // r/2 > l/2 if P
            return searchRecursive(arr, l, r, key);
        }
        // P && r - l <= 1, knowing that r > l -> r = l+1, if a[l] > key && key >= a[r] -> r - min, because previous elements are greater
        return r;
    }

    public static int searchRecursive(int[] arr, int key) {
        return searchRecursive(arr, -1, arr.length, key);
    }

    // P: key - integer, array isn't null, for all a[i] >= a[i+1]
    // Q: arr[r] <= key, r - min
    private static int searchIterative(int[] arr, int key) {
        // P
        int l = -1;
        int r = arr.length;
        // I: a[r] <= key && a[l] > key
        while(r - l > 1) {
            // P && I && r - l > 1
            int mid = (l + r) / 2;
            // P && I && r - l > 1
            if (arr[mid] > key) {
                l = mid;
                // P && I && r - mid > 1
                // P && I && r - l/2 - r/2 = r/2 - l/2 > 1
            } else {
                // a[mid] <= key -> possible answer
                r = mid;
                // P && I && mid - l > 1
                // P && I && l/2 + r/2 - l = r/2 - l/2 > 1
            }
            // P && I && r/2 - l/2 > 1 -> we halved the search area
        }
        // P && I && r - l <= 1, knowing that r > l, -> r = l + 1  -> a[r] <= key, and previous elements are greater
        return r;
    }

    public static void main(String[] args) {
        int key = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            array[i-1] = Integer.parseInt(args[i]);
        }
        //System.out.println(searchIterative(array, key));
        System.out.println(searchRecursive(array, key));
    }

    public static class BinarySearchMissing {
        public static void main(String[] args) {
            System.out.println("Hello korney!");
        }
    }
}
