package ch.heigvd.iict.and.rest.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Contact(@PrimaryKey(autoGenerate = true) var id: Long? = null,
              var name: String,
              var firstname: String?,
              var birthday : Calendar?,
              var email: String?,
              var address: String?,
              var zip: String?,
              var city: String?,
              var type: PhoneType?,
              var phoneNumber: String?) {

    override fun equals(other: Any?) =
                (other is Contact) &&
                name == other.name &&
                firstname == other.firstname &&
                birthday == other.birthday &&
                email == other.email &&
                address == other.address &&
                zip == other.zip &&
                city == other.city &&
                type == other.type &&
                phoneNumber == other.phoneNumber

    override fun toString(): String {
        return  "Contact(id: $id, name: $name, firstname: $firstname, " +
                "birthday: $birthday, email :$email, address: $address, zip: $zip, city: $city, " +
                "type: $type, phoneNumber: $phoneNumber)"
    }
}