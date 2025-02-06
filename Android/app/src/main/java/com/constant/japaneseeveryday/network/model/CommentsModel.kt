package com.constant.japaneseeveryday.network.model

import com.constant.japaneseeveryday.network.entity.CommentsResponseEntity
import com.constant.japaneseeveryday.network.entity.RepliesResponseEntity
import com.constant.japaneseeveryday.util.nonNull

class CommentsModel {
    var comments: ArrayList<CommentModel>
    var pageInfo: PageInfoModel?

    constructor(commentsResponseEntity: CommentsResponseEntity) {
        comments = ArrayList<CommentModel>()
        nonNull(commentsResponseEntity.responseData).forEach {
            comments.add(CommentModel(it))
        }
        if (commentsResponseEntity.pageInfo == null) {
            this.pageInfo = null
        } else {
            this.pageInfo = PageInfoModel(commentsResponseEntity.pageInfo)
        }
    }

    constructor(repliesResponseEntity: RepliesResponseEntity) {
        comments = ArrayList<CommentModel>()
        nonNull(repliesResponseEntity.responseData).forEach {
            comments.add(CommentModel(it))
        }
        if (repliesResponseEntity.pageInfo == null) {
            this.pageInfo = null
        } else {
            this.pageInfo = PageInfoModel(repliesResponseEntity.pageInfo)
        }
    }
}
