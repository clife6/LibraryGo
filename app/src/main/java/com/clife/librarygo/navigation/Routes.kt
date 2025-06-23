package com.clife.librarygo.navigation

//导航路由说明
object Routes {
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val LOGIN_ADMIN = "login_admin"
    const val HOME = "home"
    const val REGISTER = "register"
    const val RETRIEVE_PASSWORD = "retrieve password"
    const val SET_NEW_PASSWORD = "set_new_password"
    const val BOOK_DETAIL = "book_detail"
    const val CHANGE_AVATAR = "change_avatar"
    const val BORROW_RECORD = "borrow_record"


    fun homeWithId(id: String) = "$HOME/$id"
    fun setNewPasswordWithId(id: String) = "$SET_NEW_PASSWORD/$id"
    fun bookDetailWithIdAndUid(id: String, uid: String) = "$BOOK_DETAIL/$id/$uid"
    fun changeAvatarWithId(id: String) = "$CHANGE_AVATAR/$id"
    fun borrowingRecordsWithId(id: String) = "$BORROW_RECORD/$id"
}