package com.company;

import com.company.thread_organization.SimulationServerThread;
import com.company.thread_organization.SimulationSynchronizerThread;
import com.company.user_clients.UserClient;

public class Main {

    /**
     * Получение версии пользовательского интерфейса
     */
    public static UserClient getClient(SimulationSynchronizerThread SynchroThread) {
        return ProgramGlobals.clientVersion.client(SynchroThread);
    }

    /**
     * Инициализация потоков приложения
     */
    public static void main(String[] args) {
        //Сервер крутится в отдельном потоке, чтобы не затормаживать работу пользовательского интерфейса
        SimulationServerThread ServerThread = new SimulationServerThread();

        //Инициализация потока синхронизации вычислений сервера со временем
        SimulationSynchronizerThread SynchroThread = new SimulationSynchronizerThread(ServerThread);

        //Выбор пользовательского интерфейса
        UserClient ClientThread = getClient(SynchroThread);

        //Старт рабочих потоков
        ServerThread.start();
        SynchroThread.start();
        ClientThread.start();
    }
}

//V Передвижение волновых фронтов относительно точки их появления
//V Создание множества линейных функций воздействия на границу среды
//V Создание новых волновых фронтов на основе воздействия на границу среды
//TODO: Проверка наличия столкновения
//TODO: Приближение к месту столкновения
//TODO: Проход по списку хэндлеров столкновения

//TODO: A0 - координата начала для волнового фронта?



//TODO: DOFIGA
//Вычисление системы уравнений из четырех элементов
//Предыдущий = новый;
//Новый = следующий.
//Преобразуем два уравнения в системе так, чтобы они приняли следующий вид:
//Предыдущий = новый:
//  Равенство членов 0-й степени производной
//  Равенство членов 1-й степени производной
//Новый = следующий:
//  Равенство членов 0-й степени производной
//  Равенство членов 1-й степени производной

//В данных системах имеем четыре неизвестные: A1_i, A2_i, A0_i, V_i
//Выражаем из формулы поиска скорости волнового фронта U-,x === A1_i
//Подставляем на соответствующее место выраженное уравнение (ручками нужно предварительно всё подготовить)
//Таким образом получаем все четыре неизвестные
