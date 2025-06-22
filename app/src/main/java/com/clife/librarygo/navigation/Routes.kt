package com.clife.librarygo.navigation

//导航路由说明
object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val LOGIN_ADMIN = "login_admin"
    const val HOME = "home"
    fun homeWithId(id: String) = "$HOME/$id"
    const val REGISTER = "register"
    const val RETRIEVE_PASSWORD = "retrieve password"
    const val SET_NEW_PASSWORD = "set_new_password"
    fun setNewPasswordWithId(id: String) = "$SET_NEW_PASSWORD/$id"
    const val BOOK_DETAIL = "book_detail"
    fun bookDetailWithIdAndUid(id: String, uid: String) = "$BOOK_DETAIL/$id/$uid"
    const val CHANGE_AVATAR = "change_avatar"
    fun changeAvatarWithId(id: String) = "$CHANGE_AVATAR/$id"
}