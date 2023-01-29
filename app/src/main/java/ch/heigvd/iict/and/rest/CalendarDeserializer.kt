package ch.heigvd.iict.and.rest

import android.icu.text.SimpleDateFormat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.ParseException
import java.util.*


class CalendarDeserializer : JsonDeserializer<Calendar> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Calendar {
        val date = json.asString
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val calendar = Calendar.getInstance()
        try {
            calendar.time = format.parse(date)
        } catch (e: ParseException) {
         throw JsonParseException(e)
        }
        return calendar
    }
}