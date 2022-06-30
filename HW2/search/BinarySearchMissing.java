package search;

public class BinarySearchMissing {

    private static int searchMissingRecursive(int[] arr, int key) {
        return searchMissingRecursive(arr, -1, arr.length, key);
    }

    // P: key - integer, array isn't null, for all a[i] <= a[i+1], l < r && a[l] < key && a[r] >= key
    // Q: arr[r] == key, r - min OR if key is not in array -> return index according to Arrays.binarySearch
    private static int searchMissingRecursive(int[] arr, int l, int r, int key) {
        // a[r] >= key && a[l] < key
        if (r - l > 1) {
            // a[r] >= key && a[l] < key && r - l > 1
            int mid = (l + r) / 2;
            // a[r] >= key && a[l] < key && r - l > 1
            if (arr[mid] >= key) {
                r = mid;
                // a[r] > key && a[l] < key && mid - l > 1
                // a[r] > key && a[l] < key && l/2 + r/2 - l = r/2 - l/2 > 1
            } else {
                l = mid;
                // a[r] > key && a[l] < key && r - mid > 1
                // a[r] > key && a[l] < key && r - l/2 - r/2 = r/2 - l/2 > 1
            }
            // a[r] > key && a[l] < key && r/2 - l/2 > 1 -> we halved the search area
            // if l < r then l/2 < r/2
            return searchMissingRecursive(arr, l, r, key);
        }
        // I && r - l <= 1, knowing that r > l, -> r = l + 1  -> a[r] >= key, previous elements are < key
        // arr[r] == key -> leftmost element
        if (arr.length == 0) return -1;
        return (r < arr.length && arr[r] == key) ? r : -r-1;
    }

    // P: key - integer, array isn't null, for all a[i] <= a[i+1], l < r && a[l] < key && a[r] >= key
    // Q: arr[r] <= key, r - min
    private static int searchMissingIterative(int[] arr, int key) {
        // P
        int l = -1;
        int r = arr.length;
        // I: a[r] >= key && a[l] < key
        while(r - l > 1) {
            // P && I && r - l > 1
            int mid = (l + r) / 2;
            // P && I && r - l > 1
            if (arr[mid] >= key) {
                r = mid;
                // P && I && r - mid > 1
                // P && I && r - l/2 - r/2 = r/2 - l/2 > 1
            } else {
                l = mid;
                // P && I && mid - l > 1
                // P && I && l/2 + r/2 - l = r/2 - l/2 > 1
            }
            // P && I && r/2 - l/2 > 1 -> we halved the search area
        }
        // P && I && r - l <= 1, knowing that r > l, -> r = l + 1  -> a[r] >= key, previous elements are < key
        if (arr.length == 0) return -1;
        return (r < arr.length && arr[r] == key) ? r : -r-1;
    }



    public static void main(String[] args) {
        int key = Integer.parseInt(args[0]);
        int[] array = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            array[i-1] = Integer.parseInt(args[i]);
        }
        //System.out.println(searchMissingRecursive(array, key));
        System.out.println(searchMissingIterative(array, key));
    }
}
