/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huigou.uasp.bpm.cmd;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;

import com.huigou.exception.ApplicationException;
import com.huigou.util.StringUtil;

/**
 * 添加评论命令
 * 
 * @author gongmm
 */
public class AddCommentCmd implements Command<Comment> {

    protected String procUnitHandlerId;

    protected String bizId;

    protected String type;

    protected String message;

    public AddCommentCmd(String procUnitHandlerId, String bizId, String message) {
        this.procUnitHandlerId = procUnitHandlerId;
        this.bizId = bizId;
        this.message = message;
    }

    public AddCommentCmd(String procUnitHandlerId, String bizId, String type, String message) {
        this.procUnitHandlerId = procUnitHandlerId;
        this.bizId = bizId;
        this.type = type;
        this.message = message;
    }

    public Comment execute(CommandContext commandContext) {
        if (StringUtil.isBlank(procUnitHandlerId)) {
            throw new ApplicationException("参数“procUnitHandlerId”不能为空。");
        }
        if (StringUtil.isBlank(bizId)) {
            throw new ApplicationException("参数“bizId”不能为空。");
        }
        if (StringUtil.isBlank(message)) {
            throw new ApplicationException("参数“message”不能为空。");
        }

        String userId = Authentication.getAuthenticatedUserId();
        CommentEntity comment = new CommentEntity();
        comment.setUserId(userId);
        comment.setType((type == null) ? CommentEntity.TYPE_COMMENT : type);
        comment.setTime(Context.getProcessEngineConfiguration().getClock().getCurrentTime());
        comment.setTaskId(procUnitHandlerId);
        comment.setProcessInstanceId(bizId);
        comment.setAction(Event.ACTION_ADD_COMMENT);

        String eventMessage = message.replaceAll("\\s+", " ");
        // if (eventMessage.length() > 163) {
        // eventMessage = eventMessage.substring(0, 160) + "...";
        // }
        comment.setMessage(eventMessage);

        comment.setFullMessage(message);

        commandContext.getCommentEntityManager().insert(comment);

        return comment;
    }

    protected String getSuspendedTaskException() {
        return "Cannot add a comment to a suspended task";
    }

    protected String getSuspendedExceptionMessage() {
        return "Cannot add a comment to a suspended execution";
    }
}
