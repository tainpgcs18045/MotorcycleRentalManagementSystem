export function GetFormattedCurrency(currency) {
    let formatCurrency = new Intl.NumberFormat(undefined, {
        style: 'currency',
        currency: 'VND'
    });

    let money = formatCurrency.format(currency);
    return money;
}

export function ParseCurrencyToNumber(stringNumber) {
    var nonSeparatorNumber = stringNumber.replace(/[^0-9]/g, '');
    return parseFloat(nonSeparatorNumber);
}


export function InputNumber(StringNumber) {
    const pattern = new RegExp('^[₫\\d\\,]+$');
    if (pattern.test(StringNumber) && StringNumber !== "₫") {
        return StringNumber;
    } else {
        return "0";
    }
}