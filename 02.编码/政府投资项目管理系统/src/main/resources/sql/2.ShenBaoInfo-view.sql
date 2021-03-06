DROP TABLE
IF EXISTS CSV_RUN_SHENBAOINFO;

CREATE
OR REPLACE VIEW CSV_RUN_SHENBAOINFO AS SELECT
	S.*, T.ID_ TASK_ID,
	T.PROC_DEF_ID_,
	T.PROC_DEF_KEY_,
	T.PROC_DEF_NAME_,
	T.TASK_DEF_KEY_,
	T.NAME_ TASK_NAME,
	T.GROUP_ID_,
	T.USER_ID_,
	T.ASSIGNEE_,
	T.TRANSACTOR
FROM
	CS_SHENBAOINFO S
INNER JOIN CSV_WF_RU_TASK T ON T.PROC_INST_ID_ = S.ZONG_PROCESSID;