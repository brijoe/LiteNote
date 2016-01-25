# LiteNote
A note app for writing down everyday trifle.Once you get to use it,you will love it deeply.

# project structure
```
.
`-- org
    `-- bridge
        |-- LiteNoteApp.java
        |-- activity
        |   |-- BaseActivity.java
        |   |-- MainActivity.java
        |   |-- PubActivity.java
        |   |-- RecSendActivity.java
        |   `-- SettingActivity.java
        |-- adapter
        |   `-- NoteItemAdapter.java
        |-- config
        |   `-- Config.java
        |-- data
        |   |-- LiteNoteDB.java
        |   |-- LiteNoteDBConstants.java
        |   |-- LiteNoteOpenHelper.java
        |   `-- LiteNoteSharedPrefs.java
        |-- model
        |   `-- NoteBean.java
        |-- receiver
        |   |-- FloatingWindowReceiver.java
        |   |-- LiteNoteWidgetProvider.java
        |   `-- SyncAlarmReceiver.java
        |-- service
        |   `-- SyncService.java
        |-- task
        |   |-- CreateNoteBookTask.java
        |   |-- GetUserInfoTask.java
        |   |-- LogOutTask.java
        |   `-- SyncNoteListTask.java
        |-- util
        |   |-- CrashHandler.java
        |   |-- DateUtil.java
        |   |-- KeyBoardUtil.java
        |   |-- LogUtil.java
        |   `-- MD5Util.java
        `-- view
            |-- ConfirmDialog.java
            |-- CustomEditText.java
            |-- CustomTextView.java
            `-- FloatingWindowManager.java


```

##License
```

Copyright 2012-2014 Francesco Azzola (Surviving with Android)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
	
```