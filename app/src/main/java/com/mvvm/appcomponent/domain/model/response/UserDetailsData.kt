package com.mvvm.appcomponent.domain.model.response

data class UserDetailsData(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val website: String,
    val address :AddressData,
    val company :CompanyData

    )

data class AddressData(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: GEOData,
)

data class GEOData(
    val lat: String,
    val lng: String
)

data class CompanyData(
    val name: String,
    val catchPhrase: String,
    val bs: String,
)

/* {
    "id": 1,
    "name": "Leanne Graham",
    "username": "Bret",
    "email": "Sincere@april.biz",
    "address": {
      "street": "Kulas Light",
      "suite": "Apt. 556",
      "city": "Gwenborough",
      "zipcode": "92998-3874",
      "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
      }
    },
    "phone": "1-770-736-8031 x56442",
    "website": "hildegard.org",
    "company": {
      "name": "Romaguera-Crona",
      "catchPhrase": "Multi-layered client-server neural-net",
      "bs": "harness real-time e-markets"
    }
  }*/
