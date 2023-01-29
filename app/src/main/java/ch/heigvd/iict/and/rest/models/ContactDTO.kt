package ch.heigvd.iict.and.rest.models

import java.text.SimpleDateFormat
import java.util.*

data class ContactDTO(
    val id: Long?,
    val name: String,
    val firstname: String?,
    val birthday: String?,
    val email: String?,
    val address: String?,
    val zip: String?,
    val city: String?,
    val type: String?,
    val phoneNumber: String?
) {
    override fun toString(): String {
        return  "ContactDTO(id: $id, name: $name, firstname: $firstname, " +
                "birthday: $birthday, email :$email, address: $address, zip: $zip, city: $city, " +
                "type: $type, phoneNumber: $phoneNumber)"
    }

    // Convert from ContactDTO to Contact
    // TODO gérer les anniversaires au cas où c'est null
    fun toContact(id: Long? = null) : Contact {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        return Contact(
            id = id,
            name = this.name,
            firstname = this.firstname,
            birthday = this.birthday?.let { Calendar.getInstance().apply { time = format.parse(it) } },
            email = this.email,
            address = this.address,
            zip = this.zip,
            city = this.city,
            type = this.type?.let { PhoneType.valueOf(it) },
            phoneNumber = this.phoneNumber/*,
            status = StatusDatabase.OK,
            id_remote = this.id*/
        )
    }

    companion object {
        // Convert from Contact to ContactDTO
        fun fromContact(contact: Contact) : ContactDTO {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            return ContactDTO(
                id = contact.id, // TODO Change
                name = contact.name,
                firstname = contact.firstname,
                birthday = contact.birthday?.time?.let { format.format(it) },
                email = contact.email,
                address = contact.address,
                zip = contact.zip,
                city = contact.city,
                type = contact.type?.name,
                phoneNumber = contact.phoneNumber
            )
        }
    }
}