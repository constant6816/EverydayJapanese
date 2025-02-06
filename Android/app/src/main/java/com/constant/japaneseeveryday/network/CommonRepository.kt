package com.constant.japaneseeveryday.network

import android.content.Context
import com.constant.japaneseeveryday.extension.FormatType
import com.constant.japaneseeveryday.extension.getDateString
import com.constant.japaneseeveryday.model.DataModel
import com.constant.japaneseeveryday.network.model.*
import com.constant.japaneseeveryday.util.HHLog
import com.constant.japaneseeveryday.util.HHStyle
import com.constant.japaneseeveryday.util.MimeType
import com.constant.japaneseeveryday.util.PositionEnum
import com.constant.japaneseeveryday.util.WithdrawalReason
import com.constant.japaneseeveryday.util.nonNull
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import io.reactivex.rxjava3.core.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import java.time.LocalDateTime

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface

class CommonRepository {
    // Public Inner Class, Struct, Enum, Interface
    class ImageParam {
        var filename: String
        var data: ByteArray

        constructor(filename: String, data: ByteArray) {
            this.filename = filename
            this.data = data
        }
    }

    class Style {
        companion object {
            val none = 0
            val loadingSpinner = 1 shl 0
            val showErrorDialog = 1 shl 1
        }
    }

    // companion object
    // Public Constant
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    // Public Variable

    // ----------------------------------------------------
    // Private Variable
    private var style: HHStyle

    // ----------------------------------------------------
    // Override Method or Basic Method
    constructor(style: HHStyle) {
        this.style = style
    }

    // ----------------------------------------------------
    // Public Method
    // ----- 기본 -------

    fun config(context: Context): Observable<ConfigModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("password", "231115")
        return CommonRestAPI(context, style).config(jsonObject)
            .map { ConfigModel(it) }
    }

    fun login(
        context: Context,
        loginType: String,
        accessToken3rd: String,
    ): Observable<LoginModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("loginType", loginType)
        jsonObject.addProperty("accessToken3rd", accessToken3rd)

        return CommonRestAPI(context, style).login(jsonObject)
            .map { LoginModel(it) }
    }

    fun signup(
        context: Context,
        loginType: String,
        accessToken3rd: String,
    ): Observable<SignupModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("loginType", loginType)
        jsonObject.addProperty("accessToken3rd", accessToken3rd)
        return CommonRestAPI(context, style).signup(jsonObject)
            .map { SignupModel(it) }
    }

    fun updateDeviceToken(
        context: Context,
        deviceToken: String,
        fcmToken: String,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("deviceToken", deviceToken)
        jsonObject.addProperty("fcmToken", fcmToken)

        return CommonRestAPI(context, style).updateDeviceToken(jsonObject)
            .map { true }
    }

    fun getMember(context: Context): Observable<MemberModel> {
        return CommonRestAPI(context, style).getMember()
            .map { MemberModel(it.responseData) }
    }

    fun updateMember(
        context: Context,
        nickname: String,
        introduction: String,
        profileUrl: String?,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("nickname", nickname)
        jsonObject.addProperty("introduction", introduction)
        jsonObject.addProperty("profileUrl", profileUrl)

        return CommonRestAPI(context, style).updateMember(jsonObject)
            .map { true }
    }

    fun uploadProfileImage(
        context: Context,
        profileImageData: ByteArray,
    ): Observable<String> {
        val requestFile = profileImageData.toRequestBody(MimeType.image.toMediaType(), 0, profileImageData.size)
        val body = MultipartBody.Part.createFormData("profileImageFile", "profileImageFile.jpg", requestFile)

        return CommonRestAPI(context, style).uploadProfileImage(body)
            .map {
                nonNull(it.responseData?.fileUrl)
            }
    }

    fun withdrawal(
        context: Context,
        reason: WithdrawalReason,
        detail: String?,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("reason", reason.value)
        if (detail != null) {
            jsonObject.addProperty("detail", detail)
        }

        return CommonRestAPI(context, style).withdrawal(jsonObject)
            .map { true }
    }

    fun fetchAnnounce(context: Context): Observable<List<AnnounceModel>> {
        return CommonRestAPI(context, style).fetchAnnounce()
            .map { nonNull(it.responseData).map { AnnounceModel(it) } }
    }

    // ----- My Album -------
    fun uploadMyAlbumFile(
        context: Context,
        data: DataModel,
    ): Observable<Int> {
        val requestFile = data.data.toRequestBody(data.mimeType.toMediaType(), 0, data.data.size)
        val multipart = MultipartBody.Part.createFormData("file", data.filename, requestFile)
        return CommonRestAPI(context, style).uploadMyAlbumFile(multipart)
            .map { nonNull(it.responseData) }
    }

    fun writeMyAlbum(
        context: Context,
        content: String,
        fileIds: List<Int>,
    ): Observable<Int> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("content", content)
        val fileIdsArray = JsonArray()
        for (element in fileIds) {
            fileIdsArray.add(JsonPrimitive(element))
        }
        jsonObject.add("fileIds", fileIdsArray)

        return CommonRestAPI(context, style).writeMyAlbum(jsonObject)
            .map { nonNull(it.responseData) }
    }

    fun fetchMyAlbum(
        context: Context,
        memberId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<MyAlbumsModel> {
        return CommonRestAPI(context, style).fetchMyAlbum(memberId, cursor, size)
            .map { MyAlbumsModel(it) }
    }

    fun fetchMyAlbumAll(
        context: Context,
        cursor: Int?,
        size: Int,
    ): Observable<MyAlbumsModel> {
        return CommonRestAPI(context, style).fetchMyAlbumAll(cursor, size)
            .map { MyAlbumsModel(it) }
    }

    fun fetchMyAlbumFile(
        context: Context,
        memberId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<FilesModel> {
        return CommonRestAPI(context, style).fetchMyAlbumFile(memberId, cursor, size)
            .map { FilesModel(it) }
    }

    fun updateMyAlbum(
        context: Context,
        myAlbumId: Int,
        content: String,
        fileIds: ArrayList<Int>,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("content", content)
        val fileIdsArray = JsonArray()
        for (element in fileIds) {
            fileIdsArray.add(JsonPrimitive(element))
        }
        jsonObject.add("fileIds", fileIdsArray)

        return CommonRestAPI(context, style).updateMyAlbum(myAlbumId, jsonObject)
            .map {
                true
            }
    }

    fun deleteMyAlbum(
        context: Context,
        myAlbumId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).deleteMyAlbum(myAlbumId)
            .map {
                true
            }
    }

    fun toggleMyAlbumLike(
        context: Context,
        myAlbumId: Int,
    ): Observable<LikeModel> {
        return CommonRestAPI(context, style).toggleMyAlbumLike(myAlbumId)
            .map {
                LikeModel(it)
            }
    }

    // ----- Board -------
    fun uploadBoardFiles(
        context: Context,
        datas: ArrayList<DataModel>,
    ): Observable<List<Int>> {
        var multiparts = ArrayList<MultipartBody.Part>()
        datas.forEachIndexed { index, element ->
            val requestFile = element.data.toRequestBody(element.mimeType.toMediaType(), 0, element.data.size)
            val body = MultipartBody.Part.createFormData("files", element.filename, requestFile)
            multiparts.add(body)
        }
        return CommonRestAPI(context, style).uploadBoardFiles(multiparts)
            .map {
                nonNull(it.responseData).map { nonNull(it.id) }
            }
    }

    fun writeBoard(
        context: Context,
        clubId: Int,
        content: String,
        fileIds: Array<Int>,
        youtubeLinks: Array<String>,
    ): Observable<Int> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("clubId", clubId)
        jsonObject.addProperty("content", content)
        val fileIdsArray = JsonArray()
        for (element in fileIds) {
            fileIdsArray.add(JsonPrimitive(element))
        }
        jsonObject.add("fileIds", fileIdsArray)

        val youtubeLinksArray = JsonArray()
        for (element in youtubeLinks) {
            youtubeLinksArray.add(JsonPrimitive(element))
        }
        jsonObject.add("youtubeLinks", youtubeLinksArray)

        return CommonRestAPI(context, style).writeBoard(jsonObject)
            .map { nonNull(it.responseData) }
    }

    fun fetchBoard(
        context: Context,
        clubId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<BoardsModel> {
        return CommonRestAPI(context, style).fetchBoard(clubId, cursor, size)
            .map { BoardsModel(it) }

//        val restAPI = CommonRestAPI(context, style).fetchBoard(clubId, cursor, size)
//            .observeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//        return restAPI
        // 액세스 토큰 에러를 아래와 같이 처리 가능
//            .onErrorResumeNext {
//                val error = HHNetworkError.ofRaw(it.message!!)
//                HHLog.e(TAG, "error = ${error}")
//                when (error) {
//                    HHNetworkError.EXPIRED_ACCESS_TOKEN -> {
//                        return@onErrorResumeNext ReissueManager.getInstance().reissue().flatMap {
//                            Toast.makeText(context, "EXPIRED_ACCESS_TOKEN", Toast.LENGTH_LONG).show()
//                            return@flatMap restAPI
//                        }
//                    }
//                    else -> {
//                        HHDialog(context, null, "Network Error", "확인")
//                        return@onErrorResumeNext throw it
//                    }
//                }
//            }
//            .map { articlesEntity ->
//                BoardsModel(articlesEntity)
//            }
    }

    fun fetchHomeTownBoard(
        context: Context,
        clubId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<BoardsModel> {
        return CommonRestAPI(context, style).fetchHomeTownBoard(clubId, cursor, size)
            .map { BoardsModel(it) }
    }

    fun getBoardDetail(
        context: Context,
        boardId: Int,
    ): Observable<BoardDetailModel> {
        return CommonRestAPI(context, style).getBoardDetail(boardId)
            .map { BoardDetailModel(it) }
    }

    fun updateBoard(
        context: Context,
        boardId: Int,
        content: String,
        fileIds: Array<Int>,
        youtubeLinks: Array<String>,
    ): Observable<Int> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("content", content)
        val fileIdsArray = JsonArray()
        for (element in fileIds) {
            fileIdsArray.add(JsonPrimitive(element))
        }
        jsonObject.add("fileIds", fileIdsArray)

        val youtubeLinksArray = JsonArray()
        for (element in youtubeLinks) {
            youtubeLinksArray.add(JsonPrimitive(element))
        }
        jsonObject.add("youtubeLinks", youtubeLinksArray)
        return CommonRestAPI(context, style).updateBoard(boardId, jsonObject)
            .map { nonNull(it.responseData?.id) }
    }

    fun deleteBoard(
        context: Context,
        boardId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).deleteBoard(boardId)
            .map { true }
    }

    fun reportBoard(
        context: Context,
        boardId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).reportBoard(boardId)
            .map { true }
    }

    fun toggleBoardLike(
        context: Context,
        boardId: Int,
    ): Observable<LikeModel> {
        return CommonRestAPI(context, style).toggleBoardLike(boardId)
            .map { LikeModel(it) }
    }

    fun writeComment(
        context: Context,
        boardId: Int,
        message: String,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", message)
        return CommonRestAPI(context, style).writeComment(boardId, jsonObject)
            .map { true }
    }

    fun fetchComment(
        context: Context,
        boardId: Int,
        page: Int,
        size: Int,
    ): Observable<CommentsModel> {
        return CommonRestAPI(context, style).fetchComment(boardId, page, size)
            .map { CommentsModel(it) }
    }

    fun updateComment(
        context: Context,
        commentId: Int,
        message: String,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", message)
        return CommonRestAPI(context, style).updateComment(commentId, jsonObject)
            .map { true }
    }

    fun deleteComment(
        context: Context,
        commentId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).deleteComment(commentId)
            .map { true }
    }

    fun reportComment(
        context: Context,
        commentId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).reportComment(commentId)
            .map { true }
    }

    fun writeReply(
        context: Context,
        boardId: Int,
        commentId: Int,
        message: String,
    ): Observable<CommentModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("boardId", boardId)
        jsonObject.addProperty("message", message)
        return CommonRestAPI(context, style).writeReply(commentId, jsonObject)
            .map { CommentModel(it) }
    }

    fun fetchReply(
        context: Context,
        commentId: Int,
        page: Int,
        size: Int,
    ): Observable<CommentsModel> {
        return CommonRestAPI(context, style).fetchReply(commentId, page, size)
            .map { CommentsModel(it) }
    }

    fun updateReply(
        context: Context,
        replyId: Int,
        message: String,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", message)
        return CommonRestAPI(context, style).updateReply(replyId, jsonObject)
            .map { true }
    }

    fun deleteReply(
        context: Context,
        replyId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).deleteReply(replyId)
            .map { true }
    }

    fun reportReply(
        context: Context,
        replyId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).reportReply(replyId)
            .map { true }
    }

    // ----- Club -------
    fun uploadMediaFile(
        context: Context,
        data: DataModel,
    ): Observable<Int> {
        val requestFile = data.data.toRequestBody(data.mimeType.toMediaType(), 0, data.data.size)
        val body = MultipartBody.Part.createFormData("file", data.filename, requestFile)
        return CommonRestAPI(context, style).uploadMediaFile(body)
            .map { nonNull(it.responseData) }
    }

    fun createClub(
        context: Context,
        name: String,
        fileId: Int,
        description: String,
        province: String,
        city: String,
        area: String,
        latitude: Double,
        longitude: Double,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("fileId", fileId)
        jsonObject.addProperty("description", description)
        jsonObject.addProperty("province", province)
        jsonObject.addProperty("city", city)
        jsonObject.addProperty("area", area)
        jsonObject.addProperty("latitude", latitude)
        jsonObject.addProperty("longitude", longitude)
        return CommonRestAPI(context, style).createClub(jsonObject)
            .map { true }
    }

    fun updateClub(
        context: Context,
        clubId: Int,
        name: String,
        fileId: Int,
        description: String,
        province: String,
        city: String,
        area: String,
        latitude: Double,
        longitude: Double,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("fileId", fileId)
        jsonObject.addProperty("description", description)
        jsonObject.addProperty("province", province)
        jsonObject.addProperty("city", city)
        jsonObject.addProperty("area", area)
        jsonObject.addProperty("latitude", latitude)
        jsonObject.addProperty("longitude", longitude)
        return CommonRestAPI(context, style).updateClub(clubId, jsonObject)
            .map { true }
    }

    fun fetchClub(
        context: Context,
        page: Int,
        size: Int,
    ): Observable<ClubsModel> {
        return CommonRestAPI(context, style).fetchClub(page, size)
            .map { ClubsModel(it) }
    }

    fun fetchMyClub(
        context: Context,
        page: Int,
        size: Int,
    ): Observable<MyClubsModel> {
        return CommonRestAPI(context, style).fetchMyClub(page, size)
            .map { MyClubsModel(it) }
    }

    fun getClubDetail(
        context: Context,
        clubId: Int,
    ): Observable<ClubDetailModel> {
        return CommonRestAPI(context, style).getClubDetail(clubId)
            .map { ClubDetailModel(it) }
    }

    fun registerClub(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).registerClub(clubId)
            .map { true }
    }

    fun withdrawalClub(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).withdrawalClub(clubId)
            .map { true }
    }

    fun changePosition(
        context: Context,
        clubId: Int,
        memberId: Int,
        position: PositionEnum,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("clubId", clubId)
        jsonObject.addProperty("memberId", memberId)
        jsonObject.addProperty("position", position.value)

        return CommonRestAPI(context, style).changePosition(jsonObject)
            .map { true }
    }

    fun kickout(
        context: Context,
        clubId: Int,
        memberId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).kickout(clubId, memberId)
            .map { true }
    }

    fun deleteClub(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).deleteClub(clubId)
            .map { true }
    }

    fun uploadAlbumFile(
        context: Context,
        clubId: Int,
        data: DataModel,
    ): Observable<Int> {
        val requestFile = data.data.toRequestBody(data.mimeType.toMediaType(), 0, data.data.size)
        val body = MultipartBody.Part.createFormData("file", data.filename, requestFile)
        return CommonRestAPI(context, style).uploadAlbumFile(clubId, body)
            .map { nonNull(it.responseData) }
    }

    fun fetchAlbum(
        context: Context,
        clubId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<AlbumsModel> {
        return CommonRestAPI(context, style).fetchAlbum(clubId, cursor, size)
            .map { AlbumsModel(it) }
    }

    fun deleteAlbums(
        context: Context,
        albumIds: List<Int>,
    ): Observable<Boolean> {
        val albumIdsString =
            albumIds
                .map { it.toString() }
                .joinToString(",")
        HHLog.d(TAG, "albumIds = $albumIds")

        return CommonRestAPI(context, style).deleteAlbums(albumIdsString)
            .map {
                true
            }
    }

    fun fetchClubChatMessage(
        context: Context,
        clubId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<ClubChatMessagesModel> {
        return CommonRestAPI(context, style).fetchClubChatMessage(clubId, cursor, size)
            .map {
                ClubChatMessagesModel(it)
            }
    }

    fun addClubChatMessage(
        context: Context,
        clubId: Int,
        message: String,
    ): Observable<ChatMessageModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("clubId", clubId)
        jsonObject.addProperty("message", message)

        return CommonRestAPI(context, style).addClubChatMessage(jsonObject)
            .map {
                ChatMessageModel(it.responseData)
            }
    }

    fun canCheckout(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).canCheckout(clubId)
            .map {
                return@map true
            }
    }

    fun processAttendance(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).processAttendance(clubId)
            .map {
                return@map true
            }
    }

    fun cancelAttendance(
        context: Context,
        clubId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).cancelAttendance(clubId)
            .map {
                return@map true
            }
    }

    fun inputManualAttendance(
        context: Context,
        clubId: Int,
        startTime: LocalDateTime,
        endTime: LocalDateTime,
    ): Observable<Boolean> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startTime", startTime.getDateString(FormatType.date6))
        jsonObject.addProperty("endTime", endTime.getDateString(FormatType.date6))
        return CommonRestAPI(context, style).inputManualAttendance(clubId, jsonObject)
            .map {
                return@map true
            }
    }

    fun fetchPersonalDailyAttendance(
        context: Context,
        clubId: Int,
        memberId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Observable<List<DailyDurationModel>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startDate", startDate.getDateString(FormatType.date4))
        jsonObject.addProperty("endDate", endDate.getDateString(FormatType.date4))
        return CommonRestAPI(context, style).fetchPersonalDailyAttendance(clubId, memberId, jsonObject)
            .map {
                nonNull(it.responseData).map { DailyDurationModel(it) }
            }
    }

    fun fetchClubSumAttendance(
        context: Context,
        clubId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Observable<List<MemberDurationModel>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startDate", startDate.getDateString(FormatType.date4))
        jsonObject.addProperty("endDate", endDate.getDateString(FormatType.date4))

        return CommonRestAPI(context, style).fetchClubSumAttendance(clubId, jsonObject)
            .map {
                nonNull(it.responseData).map { MemberDurationModel(it) }
            }
    }

    fun fetchClubDailyAttendance(
        context: Context,
        clubId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Observable<List<MemberDailyDurationsModel>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startDate", startDate.getDateString(FormatType.date4))
        jsonObject.addProperty("endDate", endDate.getDateString(FormatType.date4))

        return CommonRestAPI(context, style).fetchClubDailyAttendance(clubId, jsonObject)
            .map {
                nonNull(it.responseData).map { MemberDailyDurationsModel(it) }
            }
    }

    fun fetchPersonalDetailAttendance(
        context: Context,
        clubId: Int,
        memberId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Observable<MemberAttendancesModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startDate", startDate.getDateString(FormatType.date4))
        jsonObject.addProperty("endDate", endDate.getDateString(FormatType.date4))

        return CommonRestAPI(context, style).fetchPersonalDetailAttendance(clubId, memberId, jsonObject)
            .map {
                return@map MemberAttendancesModel(it)
            }
    }

    fun fetchClubDetailAttendance(
        context: Context,
        clubId: Int,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): Observable<List<MemberAttendanceModel>> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("startDate", startDate.getDateString(FormatType.date4))
        jsonObject.addProperty("endDate", endDate.getDateString(FormatType.date4))

        return CommonRestAPI(context, style).fetchClubDetailAttendance(clubId, jsonObject)
            .map {
                nonNull(it.responseData).map { MemberAttendanceModel(it) }
            }
    }

    fun fetchApproval(context: Context): Observable<List<ApprovalModel>> {
        return CommonRestAPI(context, style).fetchApproval()
            .map {
                nonNull(it.responseData).map { ApprovalModel(it) }
            }
    }

    fun approveAttendance(
        context: Context,
        clubAttendanceId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).approveAttendance(clubAttendanceId)
            .map {
                true
            }
    }

    fun rejectAttendance(
        context: Context,
        clubAttendanceId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).rejectAttendance(clubAttendanceId)
            .map {
                true
            }
    }

    fun approveMember(
        context: Context,
        clubMemberId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).approveMember(clubMemberId)
            .map {
                true
            }
    }

    fun rejectMember(
        context: Context,
        clubMemberId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).rejectMember(clubMemberId)
            .map {
                true
            }
    }

    fun downloadAlbumFiles(
        context: Context,
        albumIds: List<Int>,
    ): Observable<ResponseBody> {
        val albumIdsString =
            albumIds
                .map { it.toString() }
                .joinToString(",")
        HHLog.d(TAG, "albumIds = $albumIds")

        return CommonRestAPI(context, style).downloadAlbumFiles(albumIdsString)
    }

    // ----- Chatting -------
    fun fetchChatRoom(context: Context): Observable<List<ChatRoomModel>> {
        return CommonRestAPI(context, style).fetchChatRoom()
            .map { nonNull(it.responseData).map { ChatRoomModel(it) } }
    }

    fun enterChat(
        context: Context,
        memberIds: Array<Int>,
        cursor: Int?,
        size: Int,
    ): Observable<ChatRoomMessagesModel> {
        val jsonObject = JsonObject()
        val memberIdsArray = JsonArray()
        for (element in memberIds) {
            memberIdsArray.add(JsonPrimitive(element))
        }
        jsonObject.add("memberIds", memberIdsArray)
        if (cursor != null) {
            jsonObject.addProperty("cursor", cursor)
        }
        jsonObject.addProperty("size", size)
        return CommonRestAPI(context, style).enterChat(jsonObject)
            .map { ChatRoomMessagesModel(it) }
    }

    fun fetchChatMessage(
        context: Context,
        chatRoomId: Int,
        cursor: Int?,
        size: Int,
    ): Observable<ChatRoomMessagesModel> {
        return CommonRestAPI(context, style).fetchChatMessage(chatRoomId, cursor, size)
            .map { ChatRoomMessagesModel(it) }
    }

    fun addChatMessage(
        context: Context,
        chatRoomId: Int,
        message: String,
    ): Observable<ChatMessageModel> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("chatRoomId", chatRoomId)
        jsonObject.addProperty("message", message)
        return CommonRestAPI(context, style).addChatMessage(jsonObject)
            .map { ChatMessageModel(it.responseData) }
    }

    fun reportChatRoom(
        context: Context,
        chatRoomId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).reportChatRoom(chatRoomId)
            .map { true }
    }

    fun blockMember(
        context: Context,
        blockeeMemberId: Int,
    ): Observable<Boolean> {
        return CommonRestAPI(context, style).blockMember(blockeeMemberId)
            .map { true }
    }
    // Private Method
}
