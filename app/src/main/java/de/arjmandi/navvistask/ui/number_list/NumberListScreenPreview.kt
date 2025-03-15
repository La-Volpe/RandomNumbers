// package de.arjmandi.navvistask.ui.number_list
//
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.tooling.preview.Preview
// import de.arjmandi.navvistask.numberdatasource.NumberDataSource
// import de.arjmandi.navvistask.ui.UiState
//
// // Preview for Loading state
// @Preview(showBackground = true)
// @Composable
// fun NumberListScreenLoadingPreview() {
//    NumberListScreen(viewModel = FakeNumberViewModel(uiState = UiState.Loading))
// }
//
// // Preview for Success state
// @Preview(showBackground = true)
// @Composable
// fun NumberListScreenSuccessPreview() {
//    val parsedNumbers = listOf(
//        ParsedNumber(sectionIndex = 0, itemValue = 3, itemCheckmark = true),
//        ParsedNumber(sectionIndex = 0, itemValue = 5, itemCheckmark = false),
//        ParsedNumber(sectionIndex = 1, itemValue = 1, itemCheckmark = true),
//        ParsedNumber(sectionIndex = 2, itemValue = 4, itemCheckmark = false)
//    )
//    NumberListScreen(viewModel = FakeNumberViewModel(uiState = UiState.Success(parsedNumbers)))
// }
//
// // Preview for Error state
// @Preview(showBackground = true)
// @Composable
// fun NumberListScreenErrorPreview() {
//    NumberListScreen(viewModel = FakeNumberViewModel(uiState = UiState.Error("Failed to fetch numbers")))
// }
//
// // Fake ViewModel for previews
// class FakeNumberViewModel(initialUiState: UiState = UiState.Loading) : NumberViewModel(FakeNumberDataSource()) {
//    private val _uiState = mutableStateOf(initialUiState)
//    override val uiState: State<UiState> = _uiState
// }
//
// // Fake NumberDataSource for previews
// class FakeNumberDataSource : NumberDataSource(FakeNumbersRepository(), FakeNumberParser())
//
// // Fake NumbersRepository for previews
// class FakeNumbersRepository : NumbersRepository {
//    override suspend fun fetchNumbers(): NumbersResponse {
//        return NumbersResponse(emptyList())
//    }
// }
//
// // Fake NumberParser for previews
// class FakeNumberParser : NumberParser {
//    override fun parseNumber(number: Int): ParsedNumber {
//        return ParsedNumber(sectionIndex = 0, itemValue = 0, itemCheckmark = false)
//    }
// }
