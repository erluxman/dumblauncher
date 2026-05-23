package com.erluxman.focuslauncher
import com.erluxman.focuslauncher.data.prefs.caffeineDoses
import com.erluxman.focuslauncher.data.prefs.logCaffeine
import com.erluxman.focuslauncher.data.prefs.clearCaffeineLog
import com.erluxman.focuslauncher.data.prefs.drinkLog
import com.erluxman.focuslauncher.data.prefs.logDrink
import com.erluxman.focuslauncher.data.prefs.clearDrinkLog
import com.erluxman.focuslauncher.data.prefs.meditationLog
import com.erluxman.focuslauncher.data.prefs.logMeditation
import com.erluxman.focuslauncher.data.prefs.clearMeditationLog
import com.erluxman.focuslauncher.data.prefs.ideaParking
import com.erluxman.focuslauncher.data.prefs.addParkedIdea
import com.erluxman.focuslauncher.data.prefs.removeParkedIdea
import com.erluxman.focuslauncher.data.prefs.clearParkedIdeas
import com.erluxman.focuslauncher.data.prefs.readingLog
import com.erluxman.focuslauncher.data.prefs.logReading
import com.erluxman.focuslauncher.data.prefs.clearReadingLog
import com.erluxman.focuslauncher.data.prefs.workoutLog
import com.erluxman.focuslauncher.data.prefs.logWorkout
import com.erluxman.focuslauncher.data.prefs.clearWorkoutLog
import com.erluxman.focuslauncher.data.prefs.commitLog
import com.erluxman.focuslauncher.data.prefs.addCommits
import com.erluxman.focuslauncher.data.prefs.clearCommitLog
import com.erluxman.focuslauncher.data.prefs.prWall
import com.erluxman.focuslauncher.data.prefs.addPersonalRecord
import com.erluxman.focuslauncher.data.prefs.removePersonalRecord
import com.erluxman.focuslauncher.data.prefs.travelAtlas
import com.erluxman.focuslauncher.data.prefs.addTravel
import com.erluxman.focuslauncher.data.prefs.removeTravel
import com.erluxman.focuslauncher.data.prefs.subscriptions
import com.erluxman.focuslauncher.data.prefs.addSubscription
import com.erluxman.focuslauncher.data.prefs.removeSubscription
import com.erluxman.focuslauncher.data.prefs.moneyIncome
import com.erluxman.focuslauncher.data.prefs.moneyExpense
import com.erluxman.focuslauncher.data.prefs.moneyAssets
import com.erluxman.focuslauncher.data.prefs.moneyLiabilities
import com.erluxman.focuslauncher.data.prefs.setMoneyIncome
import com.erluxman.focuslauncher.data.prefs.setMoneyExpense
import com.erluxman.focuslauncher.data.prefs.setMoneyAssets
import com.erluxman.focuslauncher.data.prefs.setMoneyLiabilities
import com.erluxman.focuslauncher.data.prefs.mortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.setMortalityWidgetsOptIn
import com.erluxman.focuslauncher.data.prefs.sleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.sleepWakeHour
import com.erluxman.focuslauncher.data.prefs.setSleepCutoffHour
import com.erluxman.focuslauncher.data.prefs.setSleepWakeHour
import com.erluxman.focuslauncher.data.prefs.antiBio
import com.erluxman.focuslauncher.data.prefs.setAntiBio
import com.erluxman.focuslauncher.service.launcher.FaceDownDetector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.erluxman.focuslauncher.data.prefs.UserPrefs
import com.erluxman.focuslauncher.ui.home.HomeScreen
import com.erluxman.focuslauncher.ui.onboarding.OnboardingScreen
import com.erluxman.focuslauncher.ui.theme.FocusLauncherTheme
import com.erluxman.focuslauncher.ui.focus.FocusTimerScreen
import com.erluxman.focuslauncher.ui.mantra.MantraScreen
import com.erluxman.focuslauncher.ui.transparency.TransparencyScreen
import com.erluxman.focuslauncher.ui.uninstall.UninstallScreen
import com.erluxman.focuslauncher.ui.vip.VipContactsScreen
import com.erluxman.focuslauncher.ui.boredom.BoredomScreen

sealed class Screen {
    data object Home : Screen()
    data object Menu : Screen()       // the minimal menu — single fan-out to every feature
    data object Stats : Screen()      // the sentence-only dashboard (read-only)
    data object Dashboard : Screen()  // the full ~50-card legacy home (reachable from Menu)
    data object Onboarding : Screen()
    data object Transparency : Screen()
    data object Uninstall : Screen()
    data object Vip : Screen()
    data object Focus : Screen()
    data object Mantra : Screen()
    data object Boredom : Screen()
    data object FutureSelfVideo : Screen()
    data object Breath : Screen()
    data object FeatureFlags : Screen()
    data object Export : Screen()
    data object Identity : Screen()
    data object Graduate : Screen()
    data object Wrapped : Screen()
    data object Subscriptions : Screen()
    data object SleepCorrelator : Screen()
    data object Mood : Screen()
    data object Ideas : Screen()
    data object Consumption : Screen()
    data object Journal : Screen()
    data object PrWall : Screen()
    data object Travel : Screen()
    data object Compound : Screen()
    data object Money : Screen()
    data object AntiBio : Screen()
    data object SleepWindow : Screen()
    data object DailyLogs : Screen()
    data object Highlights : Screen()
    data object Tombstones : Screen()
    data object FutureLetters : Screen()
    data object WeeklyReview : Screen()
    data object Promises : Screen()
    data object Legacy : Screen()
    data object Dilation : Screen()
    data object SadSelf : Screen()
    data object TimeMoney : Screen()
    data object Estimation : Screen()
    data object Stress : Screen()
    data object Anchor : Screen()
    data object EnergyZones : Screen()
    data object TrackStatus : Screen()
    data object Reciprocity : Screen()
    data object Groups : Screen()
    data object Backend : Screen()
    data object DualStreak : Screen()
    data object Disappointment : Screen()
    data object Profile : Screen()
    data object Feed : Screen()
    data object PreCommit : Screen()
    data object ReceiptWall : Screen()
    data object Courage : Screen()
    data object Fasting : Screen()
    data object DisappointmentInbox : Screen()
    data object TimeDonation : Screen()
    data object Sponsor : Screen()
    data object Confession : Screen()
    data object Couples : Screen()
    data object Regret : Screen()
    data object Commute : Screen()
    data object BestFriends : Screen()
    data object Sabbath : Screen()
    data object Family : Screen()
    data object Quarterly : Screen()
    data object AnnualReport : Screen()
    data object Outdoor : Screen()
    data object BudgetFuture : Screen()
    data object Substance : Screen()
    data object DreamJournal : Screen()
}

class MainActivity : ComponentActivity() {

    private var faceDownDetector: com.erluxman.focuslauncher.service.launcher.FaceDownDetector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Best-effort Firebase init; safe to call without google-services.json.
        com.erluxman.focuslauncher.backend.FirebaseInit.attemptInit(applicationContext)
        com.erluxman.focuslauncher.service.launcher.CheckInScheduler.scheduleAll(applicationContext)
        faceDownDetector = com.erluxman.focuslauncher.service.launcher.FaceDownDetector(
            applicationContext
        ) {
            val line = com.erluxman.focuslauncher.service.sad.Applause
                .maybeLine(elapsedMs = 0L, seed = System.currentTimeMillis())
                ?: "phone down. that's a win."
            android.widget.Toast.makeText(applicationContext, line, android.widget.Toast.LENGTH_SHORT).show()
        }
        setContent {
            FocusLauncherTheme {
                AppRoot()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        faceDownDetector?.start()
    }

    override fun onPause() {
        faceDownDetector?.stop()
        super.onPause()
    }
}

@Composable
private fun AppRoot() {
    val context = LocalContext.current
    val prefs = remember { UserPrefs(context.applicationContext) }
    val flagsRepo = remember {
        com.erluxman.focuslauncher.config.FeatureFlagsRepository(context.applicationContext)
    }
    val backend = remember {
        com.erluxman.focuslauncher.backend.StubBackendRepository(context.applicationContext)
    }
    val paymentRouter = remember {
        com.erluxman.focuslauncher.payment.PaymentRouter(context.applicationContext, backend, flagsRepo)
    }
    val onboardingComplete by prefs.onboardingComplete.collectAsState(initial = null)
    val legacyHome by prefs.legacyHome.collectAsState(initial = false)

    var current: Screen by remember { mutableStateOf(Screen.Home) }

    when {
        onboardingComplete == null -> Unit // First composition; render nothing for a moment.
        onboardingComplete == false || current == Screen.Onboarding -> {
            OnboardingScreen(prefs = prefs, onFinished = { current = Screen.Home })
        }
        current == Screen.Transparency -> {
            TransparencyScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Uninstall -> {
            UninstallScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Vip -> {
            VipContactsScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Focus -> {
            FocusTimerScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Mantra -> {
            MantraScreen(prefs = prefs, onBack = { current = Screen.Home })
        }
        current == Screen.Boredom -> {
            BoredomScreen(onBack = { current = Screen.Home })
        }
        current == Screen.FutureSelfVideo -> {
            com.erluxman.focuslauncher.ui.futureself.FutureSelfVideoScreen(
                onBack = { current = Screen.Home }
            )
        }
        current == Screen.Breath -> {
            com.erluxman.focuslauncher.ui.breath.BreathScreen(
                onBack = { current = Screen.Home }
            )
        }
        current == Screen.Dashboard || (current == Screen.Home && legacyHome) -> {
            HomeScreen(
                prefs = prefs,
                onOpenTransparency = { current = Screen.Transparency },
                onReplayOnboarding = { current = Screen.Onboarding },
                onOpenUninstall = { current = Screen.Uninstall },
                onOpenVip = { current = Screen.Vip },
                onOpenFocus = { current = Screen.Focus },
                onOpenMantra = { current = Screen.Mantra },
                onOpenBoredom = { current = Screen.Boredom },
                onOpenFutureSelfVideo = { current = Screen.FutureSelfVideo },
                onOpenBreath = { current = Screen.Breath }
            )
        }
        current == Screen.Menu -> {
            com.erluxman.focuslauncher.ui.home.minimal.MinimalMenuScreen(
                flagsRepo = flagsRepo,
                onBack = { current = Screen.Home },
                onOpenStats = { current = Screen.Stats },
                onOpenDashboard = { current = Screen.Dashboard },
                onOpenTransparency = { current = Screen.Transparency },
                onOpenVip = { current = Screen.Vip },
                onOpenFocus = { current = Screen.Focus },
                onOpenMantra = { current = Screen.Mantra },
                onOpenBoredom = { current = Screen.Boredom },
                onOpenBreath = { current = Screen.Breath },
                onOpenFutureSelfVideo = { current = Screen.FutureSelfVideo },
                onReplayOnboarding = { current = Screen.Onboarding },
                onOpenUninstall = { current = Screen.Uninstall },
                onOpenFeatureFlags = { current = Screen.FeatureFlags },
                onOpenExport = { current = Screen.Export },
                onOpenIdentity = { current = Screen.Identity },
                onOpenGraduate = { current = Screen.Graduate },
                onOpenWrapped = { current = Screen.Wrapped },
                onOpenSubscriptions = { current = Screen.Subscriptions },
                onOpenSleepCorrelator = { current = Screen.SleepCorrelator },
                onOpenMood = { current = Screen.Mood },
                onOpenIdeas = { current = Screen.Ideas },
                onOpenConsumption = { current = Screen.Consumption },
                onOpenJournal = { current = Screen.Journal },
                onOpenPrWall = { current = Screen.PrWall },
                onOpenTravel = { current = Screen.Travel },
                onOpenCompound = { current = Screen.Compound },
                onOpenMoney = { current = Screen.Money },
                onOpenAntiBio = { current = Screen.AntiBio },
                onOpenSleepWindow = { current = Screen.SleepWindow },
                onOpenDailyLogs = { current = Screen.DailyLogs },
                onOpenHighlights = { current = Screen.Highlights },
                onOpenTombstones = { current = Screen.Tombstones },
                onOpenFutureLetters = { current = Screen.FutureLetters },
                onOpenWeeklyReview = { current = Screen.WeeklyReview },
                onOpenPromises = { current = Screen.Promises },
                onOpenLegacy = { current = Screen.Legacy },
                onOpenDilation = { current = Screen.Dilation },
                onOpenSadSelf = { current = Screen.SadSelf },
                onOpenTimeMoney = { current = Screen.TimeMoney },
                onOpenEstimation = { current = Screen.Estimation },
                onOpenStress = { current = Screen.Stress },
                onOpenAnchor = { current = Screen.Anchor },
                onOpenEnergyZones = { current = Screen.EnergyZones },
                onOpenTrackStatus = { current = Screen.TrackStatus },
                onOpenReciprocity = { current = Screen.Reciprocity },
                onOpenGroups = { current = Screen.Groups },
                onOpenBackend = { current = Screen.Backend },
                onOpenDualStreak = { current = Screen.DualStreak },
                onOpenDisappointment = { current = Screen.Disappointment },
                onOpenProfile = { current = Screen.Profile },
                onOpenFeed = { current = Screen.Feed },
                onOpenPreCommit = { current = Screen.PreCommit },
                onOpenReceiptWall = { current = Screen.ReceiptWall },
                onOpenCourage = { current = Screen.Courage },
                onOpenFasting = { current = Screen.Fasting },
                onOpenDisappointmentInbox = { current = Screen.DisappointmentInbox },
                onOpenTimeDonation = { current = Screen.TimeDonation },
                onOpenSponsor = { current = Screen.Sponsor },
                onOpenConfession = { current = Screen.Confession },
                onOpenCouples = { current = Screen.Couples },
                onOpenRegret = { current = Screen.Regret },
                onOpenCommute = { current = Screen.Commute },
                onOpenBestFriends = { current = Screen.BestFriends },
                onOpenSabbath = { current = Screen.Sabbath },
                onOpenFamily = { current = Screen.Family },
                onOpenQuarterly = { current = Screen.Quarterly },
                onOpenAnnualReport = { current = Screen.AnnualReport },
                onOpenOutdoor = { current = Screen.Outdoor },
                onOpenBudgetFuture = { current = Screen.BudgetFuture },
                onOpenSubstance = { current = Screen.Substance },
                onOpenDreamJournal = { current = Screen.DreamJournal },
            )
        }
        current == Screen.FeatureFlags -> {
            com.erluxman.focuslauncher.ui.flags.FeatureFlagsScreen(
                repo = flagsRepo,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Export -> {
            com.erluxman.focuslauncher.ui.export.ExportScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Identity -> {
            com.erluxman.focuslauncher.ui.identity.IdentityVotingScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Graduate -> {
            com.erluxman.focuslauncher.ui.graduate.GraduateScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Wrapped -> {
            com.erluxman.focuslauncher.ui.wrapped.WrappedScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Subscriptions -> {
            com.erluxman.focuslauncher.ui.subscriptions.SubscriptionsScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.SleepCorrelator -> {
            com.erluxman.focuslauncher.ui.sleep.SleepCorrelatorScreen(
                prefs = prefs,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.Mood -> {
            com.erluxman.focuslauncher.ui.mood.MoodScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Ideas -> {
            com.erluxman.focuslauncher.ui.notes.IdeaParkingScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Consumption -> {
            com.erluxman.focuslauncher.ui.consumption.ConsumptionScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Journal -> {
            com.erluxman.focuslauncher.ui.journal.JournalScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.PrWall -> {
            com.erluxman.focuslauncher.ui.pr.PrWallScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Travel -> {
            com.erluxman.focuslauncher.ui.travel.TravelAtlasScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Compound -> {
            com.erluxman.focuslauncher.ui.compound.CompoundCurveScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Money -> {
            com.erluxman.focuslauncher.ui.money.MoneyMirrorScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.AntiBio -> {
            com.erluxman.focuslauncher.ui.antibio.AntiBioScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.SleepWindow -> {
            com.erluxman.focuslauncher.ui.sleepwindow.SleepWindowScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.DailyLogs -> {
            com.erluxman.focuslauncher.ui.logs.DailyLogsScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Highlights -> {
            com.erluxman.focuslauncher.ui.highlights.HighlightsScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Tombstones -> {
            com.erluxman.focuslauncher.ui.tombstones.TombstonesScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.FutureLetters -> {
            com.erluxman.focuslauncher.ui.letters.FutureLettersScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.WeeklyReview -> {
            com.erluxman.focuslauncher.ui.review.WeeklyReviewScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Promises -> {
            com.erluxman.focuslauncher.ui.promises.PromiseRatioScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.Legacy -> {
            com.erluxman.focuslauncher.ui.legacy.LegacyCounterScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Dilation -> {
            com.erluxman.focuslauncher.ui.dilation.TimeDilationScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.SadSelf -> {
            com.erluxman.focuslauncher.ui.sadself.SadSelfScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.TimeMoney -> {
            com.erluxman.focuslauncher.ui.timemoney.TimeMoneyScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Estimation -> {
            com.erluxman.focuslauncher.ui.estimation.EstimationScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.Stress -> {
            com.erluxman.focuslauncher.ui.stress.StressScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.Anchor -> {
            com.erluxman.focuslauncher.ui.anchor.AnchorScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.EnergyZones -> {
            com.erluxman.focuslauncher.ui.energy.EnergyZonesScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.TrackStatus -> {
            com.erluxman.focuslauncher.ui.track.TrackStatusScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Reciprocity -> {
            com.erluxman.focuslauncher.ui.reciprocity.ReciprocityScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Groups -> {
            com.erluxman.focuslauncher.ui.groups.GroupsScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Backend -> {
            com.erluxman.focuslauncher.ui.backend.BackendStatusScreen(
                backend = backend,
                paymentRouter = paymentRouter,
                onBack = { current = Screen.Menu },
            )
        }
        current == Screen.DualStreak -> {
            com.erluxman.focuslauncher.ui.dualstreak.DualStreakScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Disappointment -> {
            com.erluxman.focuslauncher.ui.disappointment.DisappointmentScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Profile -> {
            com.erluxman.focuslauncher.ui.profile.BuilderProfileScreen(prefs = prefs, backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Feed -> {
            com.erluxman.focuslauncher.ui.feed.FeedScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.PreCommit -> {
            com.erluxman.focuslauncher.ui.precommit.PreCommitScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.ReceiptWall -> {
            com.erluxman.focuslauncher.ui.receiptwall.ReceiptWallScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.Courage -> {
            com.erluxman.focuslauncher.ui.courage.CourageScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Fasting -> {
            com.erluxman.focuslauncher.ui.fasting.FastingScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.DisappointmentInbox -> {
            com.erluxman.focuslauncher.ui.inbox.DisappointmentInboxScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.TimeDonation -> {
            com.erluxman.focuslauncher.ui.timedonation.TimeDonationScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Sponsor -> {
            com.erluxman.focuslauncher.ui.sponsor.SponsorScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Confession -> {
            com.erluxman.focuslauncher.ui.confession.ConfessionScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Couples -> {
            com.erluxman.focuslauncher.ui.couples.CouplesScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Regret -> {
            com.erluxman.focuslauncher.ui.regret.RegretScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Commute -> {
            com.erluxman.focuslauncher.ui.commute.CommuteScreen(onBack = { current = Screen.Menu })
        }
        current == Screen.BestFriends -> {
            com.erluxman.focuslauncher.ui.bestfriends.BestFriendsScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Sabbath -> {
            com.erluxman.focuslauncher.ui.sabbath.SabbathScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Family -> {
            com.erluxman.focuslauncher.ui.family.FamilyScreen(backend = backend, onBack = { current = Screen.Menu })
        }
        current == Screen.Quarterly -> {
            com.erluxman.focuslauncher.ui.quarterly.QuarterlyAuditScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.AnnualReport -> {
            com.erluxman.focuslauncher.ui.annualreport.AnnualReportScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Outdoor -> {
            com.erluxman.focuslauncher.ui.outdoor.OutdoorScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.BudgetFuture -> {
            com.erluxman.focuslauncher.ui.budgetfuture.BudgetFutureScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Substance -> {
            com.erluxman.focuslauncher.ui.substance.SubstanceScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.DreamJournal -> {
            com.erluxman.focuslauncher.ui.dream.DreamJournalScreen(prefs = prefs, onBack = { current = Screen.Menu })
        }
        current == Screen.Stats -> {
            com.erluxman.focuslauncher.ui.home.minimal.MinimalStatsScreen(
                prefs = prefs,
                onReturn = { current = Screen.Home },
                onOpenTransparency = { current = Screen.Transparency },
                onOpenUninstall = { current = Screen.Uninstall },
                onOpenDashboard = { current = Screen.Dashboard },
            )
        }
        else -> {
            com.erluxman.focuslauncher.ui.home.minimal.MinimalHomeScreen(
                prefs = prefs,
                onOpenMenu = { current = Screen.Menu },
            )
        }
    }
}
