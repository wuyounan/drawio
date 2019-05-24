var AskStatus = function () {
};
AskStatus.UnAsk = 1;
AskStatus.Ask = 2;
AskStatus.Aborted = 3;
AskStatus.getDisplayName = function (status) {
    if (status == AskStatus.UnAsk)
        return "未询问";
    else if (status == AskStatus.Ask)
        return "已询问";
    else if (status == AskStatus.Aborted)
        return "已终结";
    else
        return "";
};

var AskDetailStatus = function () {
};
AskDetailStatus.UnReply = 0;
AskDetailStatus.Reply = 1;
AskDetailStatus.Aborted = 2;

AskDetailStatus.getDisplayName = function (status) {
    if (status == AskDetailStatus.UnReply)
        return "未回复";
    else if (status == AskDetailStatus.Reply)
        return "已回复";
    else if (status == AskDetailStatus.Aborted)
        return "已终结";
    else
        return "";
};

var AskKind = function () {
};
AskKind.SAMPLING = 1;
AskKind.INQUIRIES = 2;

AskKind.getDisplayName = function (status) {
    if (status == AskKind.SAMPLING)
        return "抽检";
    else if (status == AskKind.INQUIRIES)
        return "询问"; 
    else
        return "";
};

var ExceptionKind = function () { };
ExceptionKind.NONE = 1;
ExceptionKind.WARN = 2;
ExceptionKind.EXPIRE = 3;
ExceptionKind.ABNORMAL = 4;
ExceptionKind.getDisplayName = function (status) {
    if (status == ExceptionKind.NONE)
        return "";
    else if (status == ExceptionKind.WARN)
        return "临近办理限期预警";
    else if (status == ExceptionKind.EXPIRE)
        return "业务办理过期";
    else if (status == ExceptionKind.ABNORMAL)
        return "异常流程";
    else
    return "";
};