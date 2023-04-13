export function GetFormattedDatetTime(stringDate) {
    var date = new Date(stringDate);
    return date.toLocaleString("en-US");
}
