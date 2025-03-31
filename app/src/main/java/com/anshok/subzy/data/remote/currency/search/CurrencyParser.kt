package com.anshok.subzy.data.remote.currency.search

import com.anshok.subzy.data.remote.models.Currency
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

object CurrencyParser {
    fun parse(xml: String): List<Currency> {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml))

        val result = mutableListOf<Currency>()
        var event = parser.eventType

        var charCode = ""
        var name = ""
        var nominal = 1
        var value = 1.0

        while (event != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
            if (event == org.xmlpull.v1.XmlPullParser.START_TAG) {
                when (parser.name) {
                    "CharCode" -> charCode = parser.nextText()
                    "Name" -> name = parser.nextText()
                    "Nominal" -> nominal = parser.nextText().toInt()
                    "Value" -> value = parser.nextText().replace(",", ".").toDouble()
                }
            } else if (event == org.xmlpull.v1.XmlPullParser.END_TAG && parser.name == "Valute") {
                result.add(Currency(charCode, name, nominal, value))
            }
            event = parser.next()
        }

        // Добавим вручную RUB
        result.add(0, Currency("RUB", "Российский рубль", 1, 1.0))

        return result
    }
}
