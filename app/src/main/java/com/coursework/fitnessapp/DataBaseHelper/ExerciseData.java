package com.coursework.fitnessapp.DataBaseHelper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.coursework.fitnessapp.models.ExerciseModel;
import com.coursework.fitnessapp.supportclasses.TimeDuration;

import java.util.ArrayList;

public class ExerciseData
{
   ArrayList<ExerciseModel> exercises= new ArrayList<>();

   //#List of default system exercises
    @RequiresApi(api = Build.VERSION_CODES.O)
    public ExerciseData() {
        exercises.add(new ExerciseModel(
                null,
                "Lunges",
                "Start by standing with your feet shoulder-width apart and arms down at your sides.\n" +
                        "Take a step forward with your right leg and bend your right knee as you do so, stopping when your thigh is parallel to the ground. Ensure that your right knee doesn’t extend past your right foot.\n" +
                        "Push up off your right foot and return to the starting position. Repeat with your left leg. This is one rep.",
                null,
                null,
                null,
                new TimeDuration("00:01:00"),
                10,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Pushups",
                "Start in a plank position. Your core should be tight, shoulders pulled down and back, and your neck neutral.\n" +
                        "Bend your elbows and begin to lower your body down to the floor. When your chest grazes it, extend your elbows and return to the start. Focus on keeping your elbows close to your body during the movement.",
                null,
                null,
                null,
                new TimeDuration("00:00:40"),
                10,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Squats",
                "Start by standing straight, with your feet slightly wider than shoulder-width apart, and your arms at your sides.\n" +
                        "Brace your core and, keeping your chest and chin up, push your hips back and bend your knees as if you’re going to sit in a chair.\n" +
                        "Ensuring your knees don’t bow inward or outward, drop down until your thighs are parallel to the ground, bringing your arms out in front of you in a comfortable position. Pause for 1 second, then extend your legs and return to the starting position.",
                null,
                null,
                null,
                new TimeDuration("00:00:50"),
                20,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Dumbbell rows",
                "Start with a dumbbell in each hand. We recommend no more than 10 pounds for beginners.\n" +
                        "Bend forward at the waist, so your back is at a 45-degree angle to the ground. Be certain not to arch your back. Let your arms hang straight down. Ensure your neck is in line with your back and your core is engaged.\n" +
                        "Starting with your right arm, bend your elbow and pull the weight straight up toward your chest, making sure to engage your lat and stopping just below your chest.\n" +
                        "Return to the starting position and repeat with the left arm. This is one rep.",
                null,
                null,
                null,
                new TimeDuration("00:00:30"),
                10,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Burpees",
                "Start by standing upright with your feet shoulder-width apart and your arms down at your sides.\n" +
                        "With your hands out in front of you, start to squat down. When your hands reach the ground, pop your legs straight back into a pushup position.\n" +
                        "Jump your feet up to your palms by hinging at the waist. Get your feet as close to your hands as you can get, landing them outside your hands if necessary.\n" +
                        "Stand up straight, bringing your arms above your head, and jump.",
                null,
                null,
                null,
                new TimeDuration("00:01:30"),
                10,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Side planks",
                "Lie on your right side with your left leg and foot stacked on top of your right leg and foot. Prop your upper body up by placing your right forearm on the ground and elbow directly under your shoulder.\n" +
                        "Contract your core to stiffen your spine and lift your hips and knees off the ground, forming a straight line with your body.\n" +
                        "Return to start in a controlled manner. ",
                null,
                null,
                null,
                new TimeDuration("00:02:00"),
                15,
                "Default"
        ));
        exercises.add(new ExerciseModel(
                null,
                "Planks",
                "Begin in a pushup position with your hand and toes firmly planted on the ground, your back straight, and your core tight.\n" +
                        "Keep your chin slightly tucked and your gaze just in front of your hands.\n" +
                        "Take deep, controlled breaths while maintaining tension throughout your entire body, so your abs, shoulders, triceps, glutes, and quads are all engaged. ",
                null,
                null,
                null,
                new TimeDuration("00:00:30"),
                15,
                "Default"
        ));
    }
}
