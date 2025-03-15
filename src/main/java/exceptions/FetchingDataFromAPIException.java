package exceptions;

public class FetchingDataFromAPIException extends RuntimeException {

    public FetchingDataFromAPIException() {
        super("Error fetching data from API!");
    }
}
