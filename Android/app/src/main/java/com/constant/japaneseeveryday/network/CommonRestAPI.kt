package com.constant.japaneseeveryday.network

import android.content.Context
import android.provider.Settings
import com.constant.japaneseeveryday.basic.JapaneseEverydayApplication
import com.constant.japaneseeveryday.dialog.LoadingDialog
import com.constant.japaneseeveryday.model.Configuration
import com.constant.japaneseeveryday.network.entity.*
import com.constant.japaneseeveryday.singleton.AccountManager
import com.constant.japaneseeveryday.singleton.Pref
import com.constant.japaneseeveryday.singleton.PrefManager
import com.constant.japaneseeveryday.util.*
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit

// ----------------------------------------------------
// Public Outter Class, Struct, Enum, Interface
class CommonRestAPI(private val context: Context, private val style: HHStyle) {
    // ----------------------------------------------------
    // Public Inner Class, Struct, Enum, Interface
    interface RestAPI {
        // ----- 기본 -------
        // Configuration 최초 사용 함수
        @POST("/api/v1/config")
        fun config(
            @Body jsonObject: JsonObject,
        ): Call<ConfigResponseEntity>

        // 로그인 할때
        @POST("/api/v1/login") // HTTP메소드 + end point URL
        fun login(
            @Body jsonObject: JsonObject,
        ): Call<LoginResponseEntity> // 요청바디로 부터 받아온 데이터인 응답바디를 callback 타입으로 정의

        // 회원 가입
        @POST("/api/v1/signup")
        fun signup(
            @Body jsonObject: JsonObject,
        ): Call<SignupResponseEntity>

        // DeviceToken 을 서버에 전달
        @POST("/api/v1/user/push/devicetoken/update")
        fun updateDeviceToken(
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // My Profile 정보 가져오는 함수
        @GET("/api/v1/user/me")
        fun getMember(): Call<MemberResponseEntity>

        // My Profile 정보 업데이트
        @POST("/api/v1/user/update")
        fun updateMember(
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // My Profile의 사진 업로드
        @Multipart
        @POST("/api/v1/user/upload/image")
        fun uploadProfileImage(
            @Part profileImageData: MultipartBody.Part,
        ): Call<ImageResponseEntity>

        // 회원 탈퇴
        @POST("/api/v1/user/withdrawal")
        fun withdrawal(
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 일반공지사항 리스트
        @GET("/api/v1/user/announce")
        fun fetchAnnounce(): Call<AnnouncesResponseEntity>

        // ----- My Album -------
        // 마이 앨범 사진 한개 업로드
        @Multipart
        @POST("/api/v1/user/myalbum/upload")
        fun uploadMyAlbumFile(
            @Part multipart: MultipartBody.Part,
        ): Call<IntResponseEntity>

        // 글쓰기
        @POST("/api/v1/user/myalbum")
        fun writeMyAlbum(
            @Body jsonObject: JsonObject,
        ): Call<IntResponseEntity>

        // 마이 Album 나의 앨범 목록 조회
        @GET("/api/v1/user/myalbum/{memberId}")
        fun fetchMyAlbum(
            @Path("memberId")memberId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<MyAlbumsResponseEntity>

        // 마이 Album 모든 앨범 목록 조회
        @GET("/api/v1/user/myalbum/all")
        fun fetchMyAlbumAll(
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<MyAlbumsResponseEntity>

        // 마이 Album 사진 목록 조회
        @GET("/api/v1/user/myalbum/file/{memberId}")
        fun fetchMyAlbumFile(
            @Path("memberId")memberId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<FilesResponseEntity>

        // 마이 Album 업데이트
        @PATCH("/api/v1/user/myalbum/{myAlbumId}")
        fun updateMyAlbum(
            @Path("myAlbumId")myAlbumId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 마이 Album 에서 사진 목록들 삭제
        @DELETE("/api/v1/user/myalbum/{myAlbumId}")
        fun deleteMyAlbum(
            @Path("myAlbumId")myAlbumId: Int,
        ): Call<VoidResponseEntity>

        // 마이 Album 좋아요
        @GET("/api/v1/user/myalbum/{myAlbumId}/like")
        fun toggleMyAlbumLike(
            @Path("myAlbumId")myAlbumId: Int,
        ): Call<LikeResponseEntity>

        // ----- Board -------
        // 글을 게시
        @Multipart
        @POST("/api/v1/user/board/upload")
        fun uploadBoardFiles(
            @Part multiparts: List<MultipartBody.Part>,
        ): Call<FilesResponseEntity>

        // 글쓰기
        @POST("/api/v1/user/board")
        fun writeBoard(
            @Body jsonObject: JsonObject,
        ): Call<IntResponseEntity>

        // Board 목록 조회
        @GET("/api/v1/user/boards")
        fun fetchBoard(
            @Query("clubId")clubId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<BoardsResponseEntity>

        // 우리 동네 Board 목록 조회
        @GET("/api/v1/user/boards/hometown")
        fun fetchHomeTownBoard(
            @Query("clubId")clubId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<BoardsResponseEntity>

        // 글 한개 가져오기
        @GET("/api/v1/user/board/{boardId}")
        fun getBoardDetail(
            @Path("boardId") boardId: Int,
        ): Call<BoardDetailResponseEntity>

        // 글 업데이트
        @PATCH("/api/v1/user/board/{boardId}")
        fun updateBoard(
            @Path("boardId") boardId: Int,
            @Body jsonObject: JsonObject,
        ): Call<BoardDetailResponseEntity>

        // 글 삭제
        @DELETE("/api/v1/user/board/{boardId}")
        fun deleteBoard(
            @Path("boardId") boardId: Int,
        ): Call<VoidResponseEntity>

        // 글 신고
        @POST("/api/v1/user/board/{boardId}/report")
        fun reportBoard(
            @Path("boardId") boardId: Int,
        ): Call<VoidResponseEntity>

        // 좋아요
        @GET("/api/v1/user/board/{boardId}/like")
        fun toggleBoardLike(
            @Path("boardId") boardId: Int,
        ): Call<LikeResponseEntity>

        // 댓글 추가
        @POST("/api/v1/user/board/{boardId}/comment")
        fun writeComment(
            @Path("boardId") boardId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 댓글 조회
        @GET("/api/v1/user/board/{boardId}/comment")
        fun fetchComment(
            @Path("boardId") boardId: Int,
            @Query("page")page: Int,
            @Query("size")size: Int,
        ): Call<CommentsResponseEntity>

        // 댓글 업데이트
        @PATCH("/api/v1/user/board/comment/{commentId}")
        fun updateComment(
            @Path("commentId") commentId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 댓글 삭제
        @DELETE("/api/v1/user/board/comment/{commentId}")
        fun deleteComment(
            @Path("commentId") commentId: Int,
        ): Call<VoidResponseEntity>

        // 댓글 신고
        @POST("/api/v1/user/board/comment/{commentId}/report")
        fun reportComment(
            @Path("commentId") commentId: Int,
        ): Call<VoidResponseEntity>

        // 대댓글 추가
        @POST("/api/v1/user/board/comment/{commentId}/reply")
        fun writeReply(
            @Path("commentId") commentId: Int,
            @Body jsonObject: JsonObject,
        ): Call<OneReplyResponseEntity>

        // 대댓글 조회
        @GET("/api/v1/user/board/comment/{commentId}/reply")
        fun fetchReply(
            @Path("commentId") commentId: Int,
            @Query("page")page: Int,
            @Query("size")size: Int,
        ): Call<RepliesResponseEntity>

        // 대댓글 업데이트
        @PATCH("/api/v1/user/board/reply/{replyId}")
        fun updateReply(
            @Path("replyId") replyId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 대댓글 삭제
        @DELETE("/api/v1/user/board/reply/{replyId}")
        fun deleteReply(
            @Path("replyId") replyId: Int,
        ): Call<VoidResponseEntity>

        // 대댓글 신고
        @POST("/api/v1/user/board/reply/{replyId}/report")
        fun reportReply(
            @Path("replyId") replyId: Int,
        ): Call<VoidResponseEntity>

        // ----- Club -------
        // File 업로드
        @Multipart
        @POST("/api/v1/user/mediafile/upload")
        fun uploadMediaFile(
            @Part multipart: MultipartBody.Part,
        ): Call<IntResponseEntity>

        // Club 생성
        @POST("/api/v1/user/club")
        fun createClub(
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // Club 업데이트
        @POST("/api/v1/user/club/{clubId}")
        fun updateClub(
            @Path("clubId") clubId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // Club 조회
        @GET("/api/v1/user/club")
        fun fetchClub(
            @Query("page")page: Int,
            @Query("size")size: Int,
        ): Call<ClubsResponseEntity>

        // My Club 조회
        @GET("/api/v1/user/club/my")
        fun fetchMyClub(
            @Query("page")page: Int,
            @Query("size")size: Int,
        ): Call<MyClubsResponseEntity>

        // Club 한개 자세히 조회
        @GET("/api/v1/user/club/{clubId}")
        fun getClubDetail(
            @Path("clubId") clubId: Int,
        ): Call<ClubDetailResponseEntity>

        // Club 가입
        @POST("/api/v1/user/club/register/{clubId}")
        fun registerClub(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // Club 탈퇴
        @POST("/api/v1/user/club/withdrawal/{clubId}")
        fun withdrawalClub(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // Club의 Member 진급
        @POST("/api/v1/user/club/position")
        fun changePosition(
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // Club의 Member 추방
        @GET("/api/v1/user/club/kickout/{clubId}/{memberId}")
        fun kickout(
            @Path("clubId") clubId: Int,
            @Path("memberId")memberId: Int,
        ): Call<VoidResponseEntity>

        // Club 삭제
        @DELETE("/api/v1/user/club/{clubId}")
        fun deleteClub(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // 앨범 사진 한개 업로드
        @Multipart
        @POST("/api/v1/user/album/upload")
        fun uploadAlbumFile(
            @Query("clubId")clubId: Int,
            @Part multipart: MultipartBody.Part,
        ): Call<IntResponseEntity>

        // Album 목록 조회
        @GET("/api/v1/user/album")
        fun fetchAlbum(
            @Query("clubId")clubId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<AlbumsResponseEntity>

        // Club Album 에서 사진 목록들 삭제
        @DELETE("/api/v1/user/album")
        fun deleteAlbums(
            @Query("albumIds")albumIds: String,
        ): Call<VoidResponseEntity>

        // Club내에서 대화 내용 조회
        @GET("/api/v1/user/club/chat/{clubId}")
        fun fetchClubChatMessage(
            @Path("clubId") clubId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<ClubChatMessagesResponseEntity>

        // Club내에서 채팅하기
        @POST("/api/v1/user/club/chat/add")
        fun addClubChatMessage(
            @Body jsonObject: JsonObject,
        ): Call<ClubChatMessageResponseEntity>

        // 퇴근이 유효한지
        @GET("/api/v1/user/club/cancheckout/{clubId}")
        fun canCheckout(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // 출,퇴근 기록
        @GET("/api/v1/user/club/attendance/{clubId}")
        fun processAttendance(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // 근태 취소
        @GET("/api/v1/user/club/attendance/cancel/{clubId}")
        fun cancelAttendance(
            @Path("clubId") clubId: Int,
        ): Call<VoidResponseEntity>

        // 수동 입력
        @POST("/api/v1/user/club/attendance/manual/{clubId}")
        fun inputManualAttendance(
            @Path("clubId") clubId: Int,
            @Body jsonObject: JsonObject,
        ): Call<VoidResponseEntity>

        // 개인 출퇴근 기록 조회
        @POST("/api/v1/user/club/attendance/daily/personal/{clubId}/{memberId}")
        fun fetchPersonalDailyAttendance(
            @Path("clubId") clubId: Int,
            @Path("memberId") memberId: Int,
            @Body jsonObject: JsonObject,
        ): Call<DailyDurationsResponseEntity>

        // 전체 출퇴근 합계 기록 조회
        @POST("/api/v1/user/club/attendance/sum/club/{clubId}")
        fun fetchClubSumAttendance(
            @Path("clubId") clubId: Int,
            @Body jsonObject: JsonObject,
        ): Call<MemberDurationsResponseEntity>

        // 전체 출퇴근 일일 기록 조회
        @POST("/api/v1/user/club/attendance/daily/club/{clubId}")
        fun fetchClubDailyAttendance(
            @Path("clubId") clubId: Int,
            @Body jsonObject: JsonObject,
        ): Call<MemberDailyDurationsResponseEntity>

        // 개인 건별 근무 시간
        @POST("/api/v1/user/club/attendance/detail/personal/{clubId}/{memberId}")
        fun fetchPersonalDetailAttendance(
            @Path("clubId") clubId: Int,
            @Path("memberId") memberId: Int,
            @Body jsonObject: JsonObject,
        ): Call<MemberAttendancesResponseEntity>

        // 전체 건별 근무 시간
        @POST("/api/v1/user/club/attendance/detail/club/{clubId}")
        fun fetchClubDetailAttendance(
            @Path("clubId") clubId: Int,
            @Body jsonObject: JsonObject,
        ): Call<ClubMemberAttendancesResponseEntity>

        // 결재 목록 조회
        @GET("/api/v1/user/club/approval")
        fun fetchApproval(): Call<ApprovalsResponseEntity>

        // 결재 승인
        @GET("/api/v1/user/club/approval/approve/attendance/{clubAttendanceId}")
        fun approveAttendance(
            @Path("clubAttendanceId") clubAttendanceId: Int,
        ): Call<VoidResponseEntity>

        // 결재 반려
        @GET("/api/v1/user/club/approval/reject/attendance/{clubAttendanceId}")
        fun rejectAttendance(
            @Path("clubAttendanceId") clubAttendanceId: Int,
        ): Call<VoidResponseEntity>

        // 멤버가 club 가입에 결재 승인
        @GET("/api/v1/user/club/approval/approve/member/{clubMemberId}")
        fun approveMember(
            @Path("clubMemberId") clubMemberId: Int,
        ): Call<VoidResponseEntity>

        // 멤버가 Club 가입에 결재 반려
        @GET("/api/v1/user/club/approval/reject/member/{clubMemberId}")
        fun rejectMember(
            @Path("clubMemberId") clubMemberId: Int,
        ): Call<VoidResponseEntity>

        // Club Album 에서 사진 목록들 다운 받기
        @GET("/api/v1/user/album/download")
        fun downloadAlbumFiles(
            @Query("albumIds")albumIds: String,
        ): Call<ResponseBody>

        // ----- Chatting -------
        // ChatRoom List 얻어오기
        @GET("/api/v1/user/chat")
        fun fetchChatRoom(): Call<ChatRoomsResponseEntity>

        // 채팅방으로 들어가기
        @POST("/api/v1/user/chat/enter")
        fun enterChat(
            @Body jsonObject: JsonObject,
        ): Call<ChatRoomMessagesResponseEntity>

        // 채팅방의 메시지 리스트 불러오기
        @GET("/api/v1/user/chat/message/{chatRoomId}")
        fun fetchChatMessage(
            @Path("chatRoomId") chatRoomId: Int,
            @Query("cursor")cursor: Int?,
            @Query("size")size: Int,
        ): Call<ChatRoomMessagesResponseEntity>

        // 채팅하기
        @POST("/api/v1/user/chat/add")
        fun addChatMessage(
            @Body jsonObject: JsonObject,
        ): Call<ChatMessageResponseEntity>

        // 채팅룸 신고하기
        @POST("/api/v1/user/chat/{chatRoomId}/report")
        fun reportChatRoom(
            @Path("chatRoomId") chatRoomId: Int,
        ): Call<VoidResponseEntity>

        // 차단하기
        @POST("/api/v1/user/block/{blockeeMemberId}")
        fun blockMember(
            @Path("blockeeMemberId") blockeeMemberId: Int,
        ): Call<VoidResponseEntity>
    }

    // companion object
    // Public Constant

    // ----------------------------------------------------
    // Private Constant
    private val TAG = nonNull(this::class.simpleName)
    private val CONNECT_TIMEOUT = 10L
    private val WRITE_TIMEOUT = 60L
    private val READ_TIMEOUT = 60L

    // Public Variable

    // ----------------------------------------------------
    // Private Variable

    // ----------------------------------------------------
    // Override Method or Basic Method

    // ----------------------------------------------------
    // Public Method
    fun config(
        @Body jsonObject: JsonObject,
    ): Observable<ConfigResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }

        return Observable.create { emitter ->
            createRetrofit(false).config(jsonObject).enqueue(HHResponse<ConfigResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun login(
        @Body jsonObject: JsonObject,
    ): Observable<LoginResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(false).login(jsonObject).enqueue(HHResponse<LoginResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun signup(
        @Body jsonObject: JsonObject,
    ): Observable<SignupResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(false).signup(jsonObject).enqueue(HHResponse<SignupResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateDeviceToken(
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(
                true,
            ).updateDeviceToken(jsonObject).enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun getMember(): Observable<MemberResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).getMember().enqueue(HHResponse<MemberResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateMember(
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateMember(jsonObject).enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun uploadProfileImage(
        @Part profileImageData: MultipartBody.Part,
    ): Observable<ImageResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(
                true,
            ).uploadProfileImage(profileImageData).enqueue(HHResponse<ImageResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun withdrawal(
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(
                true,
            ).withdrawal(jsonObject).enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchAnnounce(): Observable<AnnouncesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(
                true,
            ).fetchAnnounce().enqueue(HHResponse<AnnouncesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun uploadMyAlbumFile(
        @Part multipart: MultipartBody.Part,
    ): Observable<IntResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).uploadMyAlbumFile(multipart)
                .enqueue(HHResponse<IntResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun writeMyAlbum(
        @Body jsonObject: JsonObject,
    ): Observable<IntResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).writeMyAlbum(jsonObject)
                .enqueue(HHResponse<IntResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchMyAlbum(
        @Path("memberId")memberId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<MyAlbumsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchMyAlbum(memberId, cursor, size)
                .enqueue(HHResponse<MyAlbumsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchMyAlbumAll(
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<MyAlbumsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchMyAlbumAll(cursor, size)
                .enqueue(HHResponse<MyAlbumsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchMyAlbumFile(
        @Path("memberId")memberId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<FilesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchMyAlbumFile(memberId, cursor, size)
                .enqueue(HHResponse<FilesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateMyAlbum(
        @Path("myAlbumId")myAlbumId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateMyAlbum(myAlbumId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteMyAlbum(
        @Path("myAlbumId")myAlbumId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteMyAlbum(myAlbumId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun toggleMyAlbumLike(
        @Path("myAlbumId")myAlbumId: Int,
    ): Observable<LikeResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).toggleMyAlbumLike(myAlbumId)
                .enqueue(HHResponse<LikeResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun uploadBoardFiles(
        @Part multiparts: List<MultipartBody.Part>,
    ): Observable<FilesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).uploadBoardFiles(multiparts)
                .enqueue(HHResponse<FilesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun writeBoard(
        @Body jsonObject: JsonObject,
    ): Observable<IntResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).writeBoard(jsonObject)
                .enqueue(HHResponse<IntResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchBoard(
        @Query("clubId")clubId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<BoardsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchBoard(clubId, cursor, size)
                .enqueue(HHResponse<BoardsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchHomeTownBoard(
        @Query("clubId")clubId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<BoardsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchHomeTownBoard(clubId, cursor, size)
                .enqueue(HHResponse<BoardsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun getBoardDetail(
        @Path("boardId") boardId: Int,
    ): Observable<BoardDetailResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).getBoardDetail(boardId)
                .enqueue(HHResponse<BoardDetailResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateBoard(
        @Path("boardId") boardId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<BoardDetailResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateBoard(boardId, jsonObject)
                .enqueue(HHResponse<BoardDetailResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteBoard(
        @Path("boardId") boardId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteBoard(boardId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun reportBoard(
        @Path("boardId") boardId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).reportBoard(boardId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun toggleBoardLike(
        @Path("boardId") boardId: Int,
    ): Observable<LikeResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).toggleBoardLike(boardId)
                .enqueue(HHResponse<LikeResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun writeComment(
        @Path("boardId") boardId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).writeComment(boardId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchComment(
        @Path("boardId") boardId: Int,
        @Query("page")page: Int,
        @Query("size")size: Int,
    ): Observable<CommentsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchComment(boardId, page, size)
                .enqueue(HHResponse<CommentsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateComment(
        @Path("commentId") commentId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateComment(commentId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteComment(
        @Path("commentId") commentId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteComment(commentId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun reportComment(
        @Path("commentId") commentId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).reportComment(commentId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun writeReply(
        @Path("commentId") commentId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<OneReplyResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).writeReply(commentId, jsonObject)
                .enqueue(HHResponse<OneReplyResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchReply(
        @Path("commentId") commentId: Int,
        @Query("page")page: Int,
        @Query("size")size: Int,
    ): Observable<RepliesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchReply(commentId, page, size)
                .enqueue(HHResponse<RepliesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateReply(
        @Path("replyId") replyId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateReply(replyId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteReply(
        @Path("replyId") replyId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteReply(replyId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun reportReply(
        @Path("replyId") replyId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).reportReply(replyId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    // ----- Club -------

    fun uploadMediaFile(
        @Part multipart: MultipartBody.Part,
    ): Observable<IntResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).uploadMediaFile(multipart)
                .enqueue(HHResponse<IntResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun createClub(
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).createClub(jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun updateClub(
        @Path("clubId") clubId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).updateClub(clubId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchClub(
        @Query("page")page: Int,
        @Query("size")size: Int,
    ): Observable<ClubsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchClub(page, size)
                .enqueue(HHResponse<ClubsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchMyClub(
        @Query("page")page: Int,
        @Query("size")size: Int,
    ): Observable<MyClubsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchMyClub(page, size)
                .enqueue(HHResponse<MyClubsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun getClubDetail(
        @Path("clubId") clubId: Int,
    ): Observable<ClubDetailResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).getClubDetail(clubId)
                .enqueue(HHResponse<ClubDetailResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun registerClub(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).registerClub(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun withdrawalClub(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).withdrawalClub(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun changePosition(
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).changePosition(jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun kickout(
        @Path("clubId") clubId: Int,
        @Query("memberId")memberId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).kickout(clubId, memberId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteClub(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteClub(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun uploadAlbumFile(
        @Query("clubId")clubId: Int,
        @Part multipart: MultipartBody.Part,
    ): Observable<IntResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).uploadAlbumFile(clubId, multipart)
                .enqueue(HHResponse<IntResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchAlbum(
        @Query("clubId")clubId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<AlbumsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchAlbum(clubId, cursor, size)
                .enqueue(HHResponse<AlbumsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun deleteAlbums(
        @Query("albumIds")albumIds: String,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).deleteAlbums(albumIds)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchClubChatMessage(
        @Path("clubId") clubId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<ClubChatMessagesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchClubChatMessage(clubId, cursor, size)
                .enqueue(HHResponse<ClubChatMessagesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun addClubChatMessage(
        @Body jsonObject: JsonObject,
    ): Observable<ClubChatMessageResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).addClubChatMessage(jsonObject)
                .enqueue(HHResponse<ClubChatMessageResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun canCheckout(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).canCheckout(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun processAttendance(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).processAttendance(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun cancelAttendance(
        @Path("clubId") clubId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).cancelAttendance(clubId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun inputManualAttendance(
        @Path("clubId") clubId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).inputManualAttendance(clubId, jsonObject)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchPersonalDailyAttendance(
        @Path("clubId") clubId: Int,
        @Path("memberId") memberId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<DailyDurationsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchPersonalDailyAttendance(clubId, memberId, jsonObject)
                .enqueue(HHResponse<DailyDurationsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchClubSumAttendance(
        @Path("clubId") clubId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<MemberDurationsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchClubSumAttendance(clubId, jsonObject)
                .enqueue(HHResponse<MemberDurationsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchClubDailyAttendance(
        @Path("clubId") clubId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<MemberDailyDurationsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchClubDailyAttendance(clubId, jsonObject)
                .enqueue(HHResponse<MemberDailyDurationsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchPersonalDetailAttendance(
        @Path("clubId") clubId: Int,
        @Path("memberId") memberId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<MemberAttendancesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchPersonalDetailAttendance(clubId, memberId, jsonObject)
                .enqueue(HHResponse<MemberAttendancesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchClubDetailAttendance(
        @Path("clubId") clubId: Int,
        @Body jsonObject: JsonObject,
    ): Observable<ClubMemberAttendancesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchClubDetailAttendance(clubId, jsonObject)
                .enqueue(HHResponse<ClubMemberAttendancesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchApproval(): Observable<ApprovalsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchApproval()
                .enqueue(HHResponse<ApprovalsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun approveAttendance(
        @Path("clubAttendanceId") clubAttendanceId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).approveAttendance(clubAttendanceId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun rejectAttendance(
        @Path("clubAttendanceId") clubAttendanceId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).rejectAttendance(clubAttendanceId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun approveMember(
        @Path("clubMemberId") clubMemberId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).approveMember(clubMemberId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun rejectMember(
        @Path("clubMemberId") clubMemberId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).rejectMember(clubMemberId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun downloadAlbumFiles(
        @Query("albumIds")albumIds: String,
    ): Observable<ResponseBody> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).downloadAlbumFiles(albumIds)
                .enqueue(HHResponse<ResponseBody>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchChatRoom(): Observable<ChatRoomsResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchChatRoom()
                .enqueue(HHResponse<ChatRoomsResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun enterChat(
        @Body jsonObject: JsonObject,
    ): Observable<ChatRoomMessagesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).enterChat(jsonObject)
                .enqueue(HHResponse<ChatRoomMessagesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun fetchChatMessage(
        @Path("chatRoomId") chatRoomId: Int,
        @Query("cursor")cursor: Int?,
        @Query("size")size: Int,
    ): Observable<ChatRoomMessagesResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).fetchChatMessage(chatRoomId, cursor, size)
                .enqueue(HHResponse<ChatRoomMessagesResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun addChatMessage(
        @Body jsonObject: JsonObject,
    ): Observable<ChatMessageResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).addChatMessage(jsonObject)
                .enqueue(HHResponse<ChatMessageResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun reportChatRoom(
        @Path("chatRoomId") chatRoomId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).reportChatRoom(chatRoomId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    fun blockMember(
        @Path("blockeeMemberId") blockeeMemberId: Int,
    ): Observable<VoidResponseEntity> {
        var loadingDialog: LoadingDialog? = null
        if (style.isInclude(CommonRepository.Style.loadingSpinner)) {
            loadingDialog = LoadingDialog(context)
            loadingDialog.show()
        }
        return Observable.create { emitter ->
            createRetrofit(true).blockMember(blockeeMemberId)
                .enqueue(HHResponse<VoidResponseEntity>(context, style, loadingDialog, emitter))
        }
    }

    // Private Method
    private fun createRetrofit(isSecured: Boolean): RestAPI {
        val client =
            OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(createHeader(isSecured))
                .addInterceptor(ErrorHandleInterceptor(context))
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
                .build()

        val retrofit =
            Retrofit.Builder() // 레트로핏 빌더 생성(생성자 호출)
                .baseUrl(Configuration.getInstance().getBaseUrl()) // 빌더 객체의 baseURL호출=서버의 메인 URL정달
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()) // gson컨버터 연동
                .build() // 객체반환

        return retrofit.create(RestAPI::class.java) // SampleService 인터페이스를 구현한 구현체
    }

    private fun createHeader(isSecured: Boolean): Interceptor {
        val accessToken = PrefManager.getInstance().getStringValue(Pref.accessToken.name)

        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            HHLog.d(
                TAG,
                "ANDROID_ID = ${Settings.Secure.getString(JapaneseEverydayApplication.context.contentResolver, Settings.Secure.ANDROID_ID)}",
            )
            HHLog.d(TAG, "AppName = ${AppInfoUtil.getAppName(JapaneseEverydayApplication.context)}")

            requestBuilder
                .addHeader("content-type", "application/json")
                .addHeader("x-client-app-os", "Android")
                .addHeader("x-server-api-ver", "v1")
                .addHeader("x-country-code", Locale.getDefault().country)
                .addHeader("x-language-code", Locale.getDefault().language)
                .addHeader("x-client-app-ver", AppInfoUtil.getVersion(JapaneseEverydayApplication.context))
                .addHeader("x-client-app-rev", "${AppInfoUtil.getVersionCode(JapaneseEverydayApplication.context)}")
                .addHeader(
                    "x-client-device-id",
                    Settings.Secure.getString(JapaneseEverydayApplication.context.contentResolver, Settings.Secure.ANDROID_ID),
                )
                .addHeader("x-model-name", DeviceInfoUtil.deviceModel)
                // .addHeader("x-service-app-name", AppInfoUtil.getAppName(AttendanceApplication.context))
                .addHeader("x-service-app-name", "Attendance")
                .addHeader("x-server-type", nonNull(PrefManager.getInstance().getStringValue(Pref.server.name)))

            val userId = AccountManager.getInstance().getUserId()
            if (userId != 0) {
                requestBuilder
                    .addHeader("x-user-id", "$userId")
            }

            if (isSecured) {
                requestBuilder
                    .addHeader("Authorization", "Bearer $accessToken")
            }

            return@Interceptor it.proceed(requestBuilder.build())
        }
    }
}
