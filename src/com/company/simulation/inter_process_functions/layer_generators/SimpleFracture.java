package com.company.simulation.inter_process_functions.layer_generators;

import com.company.simulation.simulation_variables.SimulationGlobals;
import com.company.simulation.simulation_types.layer_description.LayerDescription;
import com.company.simulation.simulation_types.enums.WaveType;

import java.util.ArrayList;

public class SimpleFracture {
    public static LayerDescription generateFastPositive(ArrayList<LayerDescription> prevLayerDescriptions, double currentX, double currentTime, WaveType waveType) {
        double speedR = SimulationGlobals.getCharacteristicsSpeedCompression();

        return generateNewLayer(prevLayerDescriptions, currentX, currentTime, speedR, waveType);

    }

    public static LayerDescription generateSlowPositive(ArrayList<LayerDescription> prevLayerDescriptions, double currentX, double currentTime, WaveType waveType) {
        double speedR = SimulationGlobals.getCharacteristicsSpeedStretching();

        return generateNewLayer(prevLayerDescriptions, currentX, currentTime, speedR, waveType);

    }

    public static LayerDescription generateFastNegative(ArrayList<LayerDescription> prevLayerDescriptions, double currentX, double currentTime, WaveType waveType) {
        double speedR = 0.0 - SimulationGlobals.getCharacteristicsSpeedCompression();

        return generateNewLayer(prevLayerDescriptions, currentX, currentTime, speedR, waveType);

    }

    public static LayerDescription generateSlowNegative(ArrayList<LayerDescription> prevLayerDescriptions, double currentX, double currentTime, WaveType waveType) {
        double speedR = 0.0 - SimulationGlobals.getCharacteristicsSpeedStretching();

        return generateNewLayer(prevLayerDescriptions, currentX, currentTime, speedR, waveType);
    }


    public static LayerDescription generateNewLayer(ArrayList<LayerDescription> prevLayerDescriptions, double currentX, double currentTime, double speedR, WaveType waveType) {

        double startTR = prevLayerDescriptions.get(1).getLayerStartTime();

        double A1L = prevLayerDescriptions.get(0).getA1();
        double A2L = prevLayerDescriptions.get(0).getA2();
        double speedL = prevLayerDescriptions.get(0).getSpeed();

        double A0R = prevLayerDescriptions.get(1).getA0();
        double A1R = prevLayerDescriptions.get(1).getA1();
        double A2R = prevLayerDescriptions.get(1).getA2();

        /*
        //?????????????? ???????????? ?????? CL = 0, Xi = 0, speedL = 0
        double A2i = (A1R + A2R * speedR - A1L) / (speedR);
        double A1i = (A1L + 0.0);
        double A0i = A0R + A1R * (startTL - startTR);
         */

        double A2i = (A1R + A2R * speedR - A1L - A2L * speedL) / (speedR - speedL);
        double A1i = (A1L + A2L * speedL - A2i * speedL);
        double A0i = A0R + A1R * (currentTime - startTR) + A2R * currentX - A2i * currentX;

        LayerDescription newLayerDescription = new LayerDescription(A0i, A1i, A2i, currentTime, waveType);

        newLayerDescription.setCurrentX(currentX);
        newLayerDescription.setSpeed(speedR);

        return newLayerDescription;
    }
}
