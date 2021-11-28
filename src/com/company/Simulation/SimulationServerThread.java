package com.company.Simulation;

import com.company.Simulation.SimulationFunctions.InterProcessComputations;
import com.company.Simulation.SimulationVariables.SimulationStatuses;
import com.company.Simulation.SimulationVariables.SimulationGlobals;

import java.util.Stack;

//Сервер НЕ ДОЛЖЕН содержать переменные, отличные от статуса работы самого сервера
public class SimulationServerThread extends Thread {

    //Инициализация стекса статусов симуляции
    //Если попытаться привести его потенциальный внешний вид,
    // образуемый в результате выполнения функций ниже, то выглядеть он будет так:
    //
    //<поле_стека> -> <статус_симуляции>
    //0 -> <empty_field> / PAUSED / DISABLED - поле приостановки выполнения операций
    //1 -> INTERPROCESS - если выполняются не операции выше, то выполняется данная операция
    //Больше двух операций существовать не может
    //
    //Такое представление весьма условно, но для понимания вполне сойдет
    Stack<SimulationStatuses> simulationStatusesStack;
    {
        simulationStatusesStack = new Stack<>();
        addInSimStatusesStack(SimulationStatuses.INTERPROCESS);
    }

    //-----------------------РАБОТА СО СТЕКОМ ОПЕРАЦИЙ ПОТОКА--------------------------

    //SETTER для стека статусов симуляции
    public void addInSimStatusesStack(SimulationStatuses simStatus) {
        simulationStatusesStack.push(simStatus);
    }

    //GETTER для стека статусов симуляции
    public SimulationStatuses getFromSimStatusesStack() {
        return simulationStatusesStack.peek();
    }

    //Выход из приостановки потока симуляции
    public void simResume() {
        if (SimulationStatuses.PAUSED == simulationStatusesStack.peek())
            simulationStatusesStack.pop();
    }

    //Вход в приостановку потока симуляции
    public void simPause() {
        if (SimulationStatuses.INTERPROCESS == simulationStatusesStack.peek())
            simulationStatusesStack.push(SimulationStatuses.PAUSED);
    }

    //Отключение потока симуляции
    public void simDisable() {
        simulationStatusesStack.push(SimulationStatuses.DISABLED);
    }

    //Выполнение следующей операции сервером
    public void doNextComputation() {
        //Так как есть потребность в том, чтобы симуляция выполнялась синхронно с таймером,
        // то добавление паузы до или после выполнения операции обязательно
        simPause();
        SimulationGlobals.setCurrentWavePicture(InterProcessComputations.getResult(SimulationGlobals.getCurrentWavePicture()));
    }

    //---------------------------------------------------------------------------------

    //Основной поток, в котором крутится сервер
    @Override
    public void run() {
        addInSimStatusesStack(SimulationStatuses.PAUSED);

        //Если симуляция не деактивирована, то ...
        while(SimulationStatuses.DISABLED != simulationStatusesStack.peek()) {
            //Если симуляция на паузе, то ждем ...
            while(SimulationStatuses.PAUSED == simulationStatusesStack.peek()) {
                onSpinWait();
            }
            //Иначе выполняем следующую операцию ...
            doNextComputation();
            //И снова попадаем на паузу
        }
    }
}