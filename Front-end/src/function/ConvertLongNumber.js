export function ConvertLongNumber(number) {
    if (number > 0) {
        number = number.toFixed(0).toString().replace(/[^0-9.]/g, '');
        if (number < 1000) {
            return number;
        }
        let si = [
            { v: 1E3, s: "K" },
            { v: 1E6, s: "M" },
            { v: 1E9, s: "B" },
            { v: 1E12, s: "T" },
            { v: 1E15, s: "P" },
            { v: 1E18, s: "E" }
        ];
        let index;
        for (index = si.length - 1; index > 0; index--) {
            if (number >= si[index].v) {
                break;
            }
        }
        return (number / si[index].v).toFixed(2).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, "$1") + si[index].s;
    } else {
        number = number.toFixed(0).toString().replace(/[^0-9.]/g, '');
        if (number < 1000) {
            return number * (-1);
        }
        let si = [
            { v: 1E3, s: "K" },
            { v: 1E6, s: "M" },
            { v: 1E9, s: "B" },
            { v: 1E12, s: "T" },
            { v: 1E15, s: "P" },
            { v: 1E18, s: "E" }
        ];
        let index;
        for (index = si.length - 1; index > 0; index--) {
            if (number >= si[index].v) {
                break;
            }
        }
        return (number / si[index].v).toFixed(2).replace(/\.0+$|(\.[0-9]*[1-9])0+$/, "$1") * (-1) + si[index].s;
    }

}