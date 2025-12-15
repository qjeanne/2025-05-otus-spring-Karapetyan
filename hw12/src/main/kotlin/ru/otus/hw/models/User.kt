package ru.otus.hw.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document(collection = "users")
data class User(
    @Id
    var id: String? = null,

    @Field(name = "username")
    var usernameValue: String,

    @Field(name = "password")
    var passwordValue: String,

    var role: String
) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_$role"))

    override fun getPassword() = passwordValue

    override fun getUsername() = usernameValue
}
