package org.firstinspires.ftc.teamcode.opmodes.redAlliance;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auto.AutoConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.opmodes.util.AutoState;


@Autonomous(name = "Red Forward Autonomous Tile 3 In")
public class RedForwardAutoTile3IN extends OpMode {
    Pose startingPose= new Pose(0,85,Math.toRadians(0));
    Pose sampleOnePose = new Pose(65, 120, Math.toRadians(0));
    Pose scoreOnePose = new Pose(12, 120, Math.toRadians(0));
    Pose sampleTwoPose = new Pose(62, 130, Math.toRadians(0));
    Pose scoreTwoPose = new Pose(18, 130, Math.toRadians(0));
    Pose sampleThreePose = new Pose(62, 134, Math.toRadians(0));
    Pose scoreThreePose = new Pose(22, 134, Math.toRadians(0));
    Pose parkPosition = AutoConstants.OBVZONE_PARKING_POSE;


    Path toFirstSample;
    Path scoreOnePath;
    Path sampleTwoPath;
    Path scoreTwoPath;
    Path sampleThreePath;
    Path scoreThreePath;
    Path scoreToParkPath;

    private Follower follower;
    private AutoState currentState = AutoState.MOVE_TO_FIRST_NEUTRAL_SAMPLE;
    private long startTime;

    @Override
    public void init() {
        toFirstSample = new Path(new BezierCurve(new Point(AutoConstants.toRed(startingPose)), new Point(AutoConstants.toRed(sampleOnePose))));
        toFirstSample.setConstantHeadingInterpolation(AutoConstants.toRed(startingPose).getHeading());

        scoreOnePath = new Path(new BezierLine(new Point(AutoConstants.toRed(sampleOnePose)), new Point(AutoConstants.toRed(scoreOnePose))));
        scoreOnePath.setConstantHeadingInterpolation(AutoConstants.toRed(sampleOnePose).getHeading());

        sampleTwoPath = new Path(new BezierCurve(new Point(AutoConstants.toRed(scoreOnePose)), new Point(AutoConstants.toRed(sampleTwoPose))));
        sampleTwoPath.setConstantHeadingInterpolation(AutoConstants.toRed(scoreOnePose).getHeading());

        scoreTwoPath = new Path(new BezierLine(new Point(AutoConstants.toRed(sampleTwoPose)), new Point(AutoConstants.toRed(scoreTwoPose))));
        scoreTwoPath.setConstantHeadingInterpolation(AutoConstants.toRed(sampleTwoPose).getHeading());

        sampleThreePath = new Path(new BezierCurve(new Point(AutoConstants.toRed(scoreTwoPose)), new Point(AutoConstants.toRed(sampleThreePose))));
        sampleThreePath.setConstantHeadingInterpolation(AutoConstants.toRed(scoreTwoPose).getHeading());

        scoreThreePath = new Path(new BezierLine(new Point(AutoConstants.toRed(sampleThreePose)), new Point(AutoConstants.toRed(scoreThreePose))));
        scoreThreePath.setConstantHeadingInterpolation(AutoConstants.toRed(sampleThreePose).getHeading());

        scoreToParkPath = new Path(new BezierCurve(new Point(AutoConstants.toRed(scoreThreePose)), new Point(AutoConstants.toRed(parkPosition))));
        scoreToParkPath.setConstantHeadingInterpolation(AutoConstants.toRed(scoreThreePose).getHeading());

        follower = new Follower(hardwareMap);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        telemetry.addData("State", currentState);
    }

    @Override
    public void start() {
        currentState = AutoState.MOVE_TO_FIRST_NEUTRAL_SAMPLE;
        startTime = System.currentTimeMillis(); // Record the start time

        // Set the initial position of the robot
        follower.setStartingPose(new Pose(144, 59, 180)); // Starting position (x = 138, y = 85, heading = 0°)


        // Follow the path to the scoring zone
        follower.followPath(toFirstSample);
    }

    @Override
    public void loop() {
        follower.update();
        switch (currentState) {
            case MOVE_TO_FIRST_NEUTRAL_SAMPLE:
                if (!follower.isBusy()) {
                    currentState = AutoState.PUSH_FIRST_NEUTRAL_SAMPLE;
                    follower.followPath(toFirstSample);
                }
                break;

            case PUSH_FIRST_NEUTRAL_SAMPLE:
                follower.followPath(scoreOnePath);
                if (!follower.isBusy()) {
                    currentState = AutoState.MOVE_TO_SECOND_NEUTRAL_SAMPLE;
                }
                break;

            case MOVE_TO_SECOND_NEUTRAL_SAMPLE:
                follower.followPath(sampleTwoPath);
                if (!follower.isBusy()) {
                    currentState = AutoState.PUSH_SECOND_NEUTRAL_SAMPLE;
                }
                break;

            case PUSH_SECOND_NEUTRAL_SAMPLE:
                follower.followPath(scoreTwoPath);
                if (!follower.isBusy()) {
                    currentState = AutoState.MOVE_TO_THIRD_NEUTRAL_SAMPLE;
                }
                break;

            case MOVE_TO_THIRD_NEUTRAL_SAMPLE:
                follower.followPath(sampleThreePath);
                if (!follower.isBusy()) {
                    currentState = AutoState.PUSH_THIRD_NEUTRAL_SAMPLE;
                }
                break;

            case PUSH_THIRD_NEUTRAL_SAMPLE:
                follower.followPath(scoreThreePath);
                if (!follower.isBusy()) {
                    currentState = AutoState.MOVE_TO_OBSERVATION_ZONE;
                }
                break;


            case MOVE_TO_OBSERVATION_ZONE:
                follower.followPath(scoreToParkPath);
                if (!follower.isBusy()) {
                    currentState = AutoState.COMPLETE;
                }
                break;

            case COMPLETE:
                follower.breakFollowing();
                break;
        }

        telemetry.addData("State", currentState);
        telemetry.update();
    }

    public void stop() {
        // When the OpMode ends, ensure all actions are stopped
        follower.breakFollowing(); // Use breakFollowing to stop all motors
    }
}
