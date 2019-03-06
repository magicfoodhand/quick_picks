class DateFormatter {
    static format(date) {
        const options = {
            weekday: "long", year: "numeric", month: "short",
            day: "numeric", hour: "2-digit", minute: "2-digit"
        };
        return ( new Date(date).toLocaleTimeString("en-us", options) );
    }
}

export default DateFormatter;