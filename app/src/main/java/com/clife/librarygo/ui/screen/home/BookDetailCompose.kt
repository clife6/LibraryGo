package com.clife.librarygo.ui.screen.home

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.clife.librarygo.ui.theme.LibraryGoTheme
import com.clife.librarygo.viewmodel.home.BookDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailCompose(
    navController: NavController,
    id: String,
    uid: String,
    viewModel: BookDetailViewModel = viewModel(),
) {
    val bookDetail by viewModel.bookDetail.collectAsState()
    val bookDetailError by viewModel.bookDetailError.collectAsState()
    class DescriptionState {
        var contentExpanded by mutableStateOf(false)
        var authorExpanded by mutableStateOf(false)
    }
    var descriptionState = remember { DescriptionState() }

    LaunchedEffect(uid) {
        viewModel.setBookDetail(uid)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {}) {
                        Text("加入书架")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {}) {
                        Text("去借阅")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp)
            ) {
                if (bookDetailError != null) {
                    Text(
                        text = bookDetailError ?: "",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(bookDetail?.coverUrl)
                        .crossfade(true)
                        .listener(
                            onError = { _, throwable ->
                                Log.e(
                                    "Coil",
                                    "加载失败: ${throwable.throwable.message}"
                                )
                            }
                        )
                        .build(),
                    contentDescription = "书籍封面",
                    modifier = Modifier
                        .width(200.dp)
                        .aspectRatio(2f / 3f),
                    error = rememberVectorPainter(Icons.Default.Book),
                    contentScale = ContentScale.FillWidth
                )
            }
            Text(
                text = "《${if (bookDetail?.title.isNullOrEmpty()) "未知书名" else bookDetail?.title}》",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = (if (bookDetail?.author.isNullOrEmpty()) "未知作者" else bookDetail?.author).toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                DescriptionItem(
                    title = "内容简介",
                    content = "【内容简介】\n最贪吃的大汉使者唐蒙，\n来到了最会吃的南越之国。\n这里食材丰富，简直就是饕餮之徒的梦想之地。然而，在美食背后，却涌动着南北对峙、族群隔阂、权位争斗、国策兴废……种种波谲云诡，竟比岭南食材的风味更加复杂。\n这个懒散的大汉使者，身陷岭南的政争漩涡。他唯 一能信赖的伙伴，只有食物；唯一的破局之法，只有追求极致美食的心。\n谁都没想到，那一缕微妙滋味，竟关乎大汉与南越国运，乃至于整个中华版图……\n【编辑推荐】\n《食南之徒》是马伯庸2024年全新长篇历史小说。贪吃的汉使唐蒙来到饕餮圣地南越，没想到卷入南越宫廷的政争之中，南北对峙、族群隔阂、权位争斗、国策兴废……时局波谲云密，他能信赖的只有美食，能够破局的也只有那颗追求极致美食的心。更没人想到，唐蒙敏感的味觉竟然拓展了大汉对中华版图的认知……\n美食不会骗人，每个人在它面前，都会露出本性。\n1.马伯庸2024年全新长篇历史小说。\n2.西汉“吃货”唐蒙出使南越，从“躺平”到“趟平”的故事。\n3.一味不知原料的酱料，一枚原生北土的枣核，勾连出中华版图变迁的一段隐秘传奇，西南丝绸之路开拓史竟也由此展开。\n4.知名画家施晓颉绘制封面，随书附赠五折六联全彩明信片插图。",
                    expanded = descriptionState.contentExpanded,
                    onExpandClick = { descriptionState.contentExpanded = !descriptionState.contentExpanded },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                DescriptionItem(
                    title = "作者简介",
                    content = "余华，1960年出生，1983年开始写作。至今已经出版长篇小说4部，中短篇小说集6部，随笔集4部。主要作品有《兄弟》《活着》《许三观卖血记》《在细雨中呼喊》等。其作品已被翻译成20多种语言在美国、英国、法国、德国、意大利、西班牙、荷兰、瑞典、挪威、希腊、俄罗斯、保加利亚、匈牙利、捷克、塞尔维亚、斯洛伐克、波兰、巴西、以色列、日本、韩国、越南、泰国和印度等国出版。曾获意大利格林扎纳·卡佛文学奖（1998年）、法国文学和艺术骑士勋章（2004年）、中华图书特殊贡献奖（2005年）、法国国际信使外国小说奖（2008年）等。",
                    expanded = descriptionState.authorExpanded,
                    onExpandClick = { descriptionState.authorExpanded = !descriptionState.authorExpanded }
                )
            }
            (if (bookDetail?.contentSummary.isNullOrEmpty()) "未找到相关信息" else bookDetail?.contentSummary).toString()
            (if (bookDetail?.authorSummary.isNullOrEmpty()) "未找到相关信息" else bookDetail?.authorSummary).toString()
            val bookInfo = listOf(
                "出版社" to bookDetail?.publisher,
                "定价" to bookDetail?.price,
                "页数" to (bookDetail?.pages?.toString() ?: ""),
                "出版年" to bookDetail?.publicationYear,
                "丛书" to bookDetail?.series,
                "装帧" to bookDetail?.binding,
                "出品方" to bookDetail?.productionCompany,
                "ISBN" to bookDetail?.isbn
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                bookInfo.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = if (value.isNullOrEmpty()) "暂无信息" else value,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionItem(
    title: String,
    content: String,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val maxLines = if (expanded) Int.MAX_VALUE else 5
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        BoxWithConstraints(
            modifier = Modifier.padding(16.dp)
        ) {
            val widthPx = with(LocalDensity.current) { this@BoxWithConstraints.maxWidth.toPx().toInt() }
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onExpandClick() }
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "收起" else "展开",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis
                )
                if (content.lineCount(15.sp, LocalDensity.current, widthPx) > 5) {
                    TextButton(
                        onClick = onExpandClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = if (expanded) "收起" else "查看更多",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

fun String.lineCount(
    textSize: TextUnit,
    density: Density,
    widthPx: Int
): Int {
    val paint = android.text.TextPaint().apply {
        this.textSize = with(density) { textSize.toPx() }
    }
    val layout = android.text.StaticLayout.Builder
        .obtain(this, 0, length, paint, widthPx)
        .build()
    return layout.lineCount
}

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun PreviewBookDetailCompose() {
    LibraryGoTheme {
        BookDetailCompose(
            navController = rememberNavController(),
            "1",
            "1",
        )
    }
}