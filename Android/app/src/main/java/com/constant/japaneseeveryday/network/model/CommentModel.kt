package com.constant.japaneseeveryday.network.model

import android.os.Parcelable
import com.constant.japaneseeveryday.extension.toLocalDateTime
import com.constant.japaneseeveryday.network.entity.CommentsResponseEntity
import com.constant.japaneseeveryday.network.entity.OneReplyResponseEntity
import com.constant.japaneseeveryday.network.entity.RepliesResponseEntity
import com.constant.japaneseeveryday.util.nonNull
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
class CommentModel(
    var commentType: CommentType,
    var id: Int,
    var status: StatusType,
    var message: String,
    var replyCount: Int,
    var createdDate: LocalDateTime,
    var member: MemberModel,
    var replies: ArrayList<CommentModel>?,
    var pageInfo: PageInfoModel?,
) : Parcelable {
    enum class CommentType(val value: String) {
        comment("comment"),
        reply("reply"),
        ;

        companion object {
            val rawToEnum =
                mapOf(
                    CommentType.comment.value to CommentType.comment,
                    CommentType.reply.value to CommentType.reply,
                )

            fun ofRaw(raw: String): CommentType {
                return rawToEnum[raw] ?: CommentType.comment
            }
        }
    }

    enum class StatusType(val value: String) {
        none("NONE"),
        valid("VALID"),
        deleted("DELETED"),
        ;

        companion object {
            val rawToEnum =
                mapOf(
                    StatusType.none.value to StatusType.none,
                    StatusType.valid.value to StatusType.valid,
                    StatusType.deleted.value to StatusType.deleted,
                )

            fun ofRaw(raw: String): StatusType {
                return rawToEnum[raw] ?: StatusType.none
            }
        }
    }

    constructor() : this(
        CommentType.comment,
        0,
        StatusType.none,
        "",
        0,
        LocalDateTime.now(),
        MemberModel(),
        null,
        null,
    ) {
    }

    constructor(commentEntity: CommentsResponseEntity.CommentEntity?) : this(
        CommentType.comment,
        nonNull(commentEntity?.id),
        StatusType.ofRaw(nonNull(commentEntity?.status)),
        nonNull(commentEntity?.message),
        nonNull(commentEntity?.replyCount),
        nonNull(commentEntity?.createdDate).toLocalDateTime(),
        MemberModel(commentEntity?.member),
        null,
        null,
    ) {
    }

    constructor(replyEntity: RepliesResponseEntity.ReplyEntity?) : this(
        CommentType.reply,
        nonNull(replyEntity?.id),
        StatusType.ofRaw(nonNull(replyEntity?.status)),
        nonNull(replyEntity?.message),
        0,
        nonNull(replyEntity?.createdDate).toLocalDateTime(),
        MemberModel(replyEntity?.member),
        null,
        null,
    ) {
    }

    constructor(oneReplyResponseEntity: OneReplyResponseEntity?) : this(
        CommentType.reply,
        nonNull(oneReplyResponseEntity?.responseData?.id),
        StatusType.ofRaw(nonNull(oneReplyResponseEntity?.responseData?.status)),
        nonNull(oneReplyResponseEntity?.responseData?.message),
        0,
        nonNull(oneReplyResponseEntity?.responseData?.createdDate).toLocalDateTime(),
        MemberModel(oneReplyResponseEntity?.responseData?.member),
        null,
        null,
    ) {
    }

    constructor(comment: CommentModel) : this(
        comment.commentType,
        comment.id,
        comment.status,
        comment.message,
        comment.replyCount,
        comment.createdDate,
        comment.member,
        ArrayList<CommentModel>(),
        comment.pageInfo,
    ) {
        nonNull(comment.replies).forEach { element ->
            this.replies?.add(element)
        }
    }

    fun getRepliesCount(): Int {
        replies?.let {
            return it.size
        }
        return 0
    }

    companion object {
        class CommentInfo {
            var parent: CommentModel?
            var loadCount: Int // 현재 로딩된 갯수
            var index: Int // 현재 인덱스
            var curComment: CommentModel

            constructor(parent: CommentModel?, loadCount: Int, index: Int, curComment: CommentModel) {
                this.parent = parent
                this.loadCount = loadCount
                this.index = index
                this.curComment = curComment
            }
            constructor() {
                parent = null
                loadCount = 0
                index = 0
                curComment = CommentModel()
            }
        }

        fun getCommentReplyCount(comments: ArrayList<CommentModel>): Int {
            var commentReplyCount = 0
            for (comment in comments) {
                commentReplyCount += comment.getRepliesCount()
            }
            commentReplyCount += comments.size
            return commentReplyCount
        }

        fun getCommentInfo(
            comments: ArrayList<CommentModel>,
            index: Int,
        ): CommentInfo {
            var curIndex = 0
            comments.forEach { comment ->
                var curComment = comment
                if (index == curIndex) {
                    return CommentInfo(null, comments.size, index, comment)
                }
                curIndex += 1
                val replyCount = comment.getRepliesCount()
                if (index < curIndex + replyCount) {
                    return CommentInfo(comment, replyCount, index - curIndex, comment.replies!![index - curIndex])
                }
                curIndex += replyCount
            }
            // 에러 났을때
            return CommentInfo(null, 0, 0, CommentModel())
        }
    }
}
