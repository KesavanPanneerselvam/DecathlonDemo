package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import components.fields.ButtonChatOption
import components.fields.DotsFlashing
import components.fields.SpaceMedium
import components.fields.SpaceSmall
import components.fields.TextMedium
import components.layouts.ScreenLayoutWithActionBar
import data.chat.ChatButtonContent
import data.chat.ChatButtonItem
import data.chat.ChatDateItem
import data.chat.ChatItem
import data.chat.ChatLoadingItem
import data.chat.ChatTextItem
import data.chat.MessageType
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.delay

data class Item(val text: String,val checked: Boolean)
private lateinit var list: SnapshotStateList<ChatItem>
@Composable
fun HomeScreen(onBack: () -> Unit) {
    ScreenLayoutWithActionBar(title = "Home", onBackPressed = { /*onBack.invoke()*/ }) {

        list = remember { mutableStateListOf<ChatItem>() }
        var textValue by remember { mutableStateOf(TextFieldValue()) }
        val movableItems = list.map { item ->
            movableContentOf {
                AddChatItem(item)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp)) {
                    list.forEachIndexed { index, item ->
                        movableItems[index]()
                    }
                }
            }
            // Buttons content
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        list.clear()
                        list.add(ChatTextItem("Hi Kesavan!\nWelcome to JU Agri ChatBot..."))
                        list.add(
                            ChatButtonItem(
                                "Please select the issue type",
                                listOf(
                                    ChatButtonContent(101, "Packing related Issue"),
                                    ChatButtonContent(102, "Product quality issue")
                                )
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = 8.dp).weight(1f)
                ) {
                    Text(text = "Reset")
                }
                Button(
                    onClick = {
                        list.clear()
                        list.add(ChatTextItem("Hi Kesavan!\nWelcome to JU Agri ChatBot..."))
                        list.add(
                            ChatButtonItem(
                                "Please select the issue type",
                                listOf(
                                    ChatButtonContent(101, "Packing related Issue"),
                                    ChatButtonContent(102, "Product quality issue")
                                )
                            )
                        )
                    },
                    modifier = Modifier.padding(horizontal = 8.dp).weight(1f)
                ) {
                    Text(text = "Submit")
                }
            }
        }
        chatInit()




    /*list = remember { mutableStateListOf<ChatItem>() }
        var textValue by remember { mutableStateOf(TextFieldValue()) }
        val movableItems = list.map { item ->
            movableContentOf {
                AddChatItem(item)
            }
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp)) {
            list.forEachIndexed { index, item ->
                movableItems[index]()
            }
        }
        chatInit()*/
    }
}

/*@Composable
fun DatePickerDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize()
                //.height(200.dp)
                .padding(16.dp),
            //shape = RoundedCornerShape(16.dp),
        ) {
            EpicDatePicker(
                state = rememberEpicDatePickerState(
                    selectionMode = EpicDatePickerState.SelectionMode.Single()
                )
            )
        }
    }
}*/


@Composable
private fun AddChatItem(item: ChatItem){
    if (item.type == MessageType.LOADING) {
        ChatItemLayout(item)
    } else if (item.isFromMe) {
        Row {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = if (item.isFromMe) chatBubbleShape1 else chatBubbleShape2
                ) {
                    ChatItemLayout(item)
                }
            }
        }
        SpaceMedium()
    } else {
        Column() {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = if (item.isFromMe) chatBubbleShape1 else chatBubbleShape2
            ) {
                ChatItemLayout(item)
            }
        }
        SpaceMedium()
    }
}

@Composable
private fun chatInit(){
    list.clear()
    LaunchedEffect(Unit) {
        list.add(ChatLoadingItem())

        delay(1000)
        list.add(ChatTextItem("Hi Kesavan!\nWelcome to JU Agri ChatBot..."))

        delay(1000)
        list.add(ChatLoadingItem())

        delay(1000)
        list.add(
            ChatButtonItem(
                "Please select the issue type",
                listOf(
                    ChatButtonContent(101, "Packing related Issue"),
                    ChatButtonContent(102, "Product quality issue")
                )
            )
        )
    }
}

private val packageIssues = listOf(
    ChatButtonContent(1001, "Leakage"),
    ChatButtonContent(1002, "Breakage"),
    ChatButtonContent(1003, "Labe"),
    ChatButtonContent(1004, "Shortage")
)

private val productIssues = listOf(
    ChatButtonContent(2001, "Puffed"),
    ChatButtonContent(2002, "Sedimentation"),
    ChatButtonContent(2003, "Efficacy"),
    ChatButtonContent(2004, "Crop Burning")
)

@Composable
private fun ChatItemLayout(item: ChatItem){
    when(item.type){
        MessageType.TEXT->{
            TextMedium(
                text = item.message,
                modifier = Modifier.padding(8.dp)
            )
        }
        MessageType.BUTTON->{
            (item as ChatButtonItem).let { buttonItem ->
                Column(modifier = Modifier.padding(8.dp)) {
                    if(buttonItem.message.isNotEmpty()) {
                        TextMedium(buttonItem.message)
                    }
                    val isButtonEnabled = remember { mutableStateOf(buttonItem.isNotSelected) }
                    buttonItem.buttons.forEach { button ->
                        SpaceSmall()
                        ButtonChatOption(button, isButtonEnabled) {
                            list.add(ChatTextItem(button.title, isFromMe = true))
                            if(button.id == 101) {
                                list.add(ChatButtonItem(buttons = packageIssues))
                            }else if(button.id == 102) {
                                list.add(ChatButtonItem(buttons = productIssues))
                            }else{
                                list.add(ChatDateItem(isFromMe = true))
                            }
                            buttonItem.isNotSelected = false
                            isButtonEnabled.value = false
                        }
                    }
                }
            }
        }
        MessageType.IMAGE->{

        }
        MessageType.LOADING->{
            (item as ChatLoadingItem).let{
                val showLoading = remember { mutableStateOf(item.isLoading) }
                if(showLoading.value){
                    DotsFlashing()
                }
                LaunchedEffect(kotlin.Unit) {
                    delay(800)
                    showLoading.value = false
                    item.isLoading = false
                }
            }
        }
        MessageType.DATE->{
            (item as ChatDateItem).let {
                val datePickerFlag = remember { mutableStateOf(false) }
                val selectedDate = remember { mutableStateOf("") }
                if(datePickerFlag.value){
                    OpenDatePicker(datePickerFlag){
                        /*list.filterIndexed { index, chatItem ->
                            if(chatItem.type == MessageType.DATE){
                                list[index].message = it
                            }
                            true
                        }*/
                        selectedDate.value = it
                        item.message = it
                        list.add(ChatTextItem("Thanks for contacting us. We will resolve the issue shortly..."))
                    }
                }
                if(item.message.isEmpty()){
                    Button(onClick = {datePickerFlag.value = true}){
                        Text("Select Date", modifier = Modifier.padding(8.dp))
                    }
                }else{
                    TextMedium(item.message, modifier = Modifier.padding(8.dp), color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

private val chatBubbleShape2 = RoundedCornerShape(1.dp, 20.dp, 20.dp, 20.dp)

private val chatBubbleShape1 = RoundedCornerShape(20.dp, 20.dp, 1.dp, 20.dp)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenDatePicker(openDialog: MutableState<Boolean>,onDateSelected: (String) -> Unit) {
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = derivedStateOf { datePickerState.selectedDateMillis != null }
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            openDialog.value = false
                            onDateSelected.invoke(convertLongToTime(it))
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun convertLongToTime(time: Long): String = GMTDate(time).let { "${it.dayOfMonth}-${it.month.value}-${it.year}"}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleBottomSheetScaffoldSample() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Swipe up to expand sheet")
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Sheet content")
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                    }
                ) {
                    Text("Click to collapse sheet")
                }
            }
        }) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            Text("Scaffold Content")
        }
    }
}*/
