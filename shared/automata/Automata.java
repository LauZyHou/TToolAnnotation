/* Copyright or (C) or Copr. GET / ENST, Telecom-Paris, Ludovic Apvrille
 *
 * ludovic.apvrille AT enst.fr
 *
 * This software is a computer program whose purpose is to allow the
 * edition of TURTLE analysis, design and deployment diagrams, to
 * allow the generation of RT-LOTOS or Java code from this diagram,
 * and at last to allow the analysis of formal validation traces
 * obtained from external tools, e.g. RTL from LAAS-CNRS and CADP
 * from INRIA Rhone-Alpes.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */


package automata;

import java.util.LinkedList;
import java.util.ListIterator;


/**
 * Class Automata
 * Creation: 23/03/2006
 *
 * @author Ludovic APVRILLE
 * @version 1.0 23/03/2006
 */
//自动机
public class Automata {
    private State initState;//唯一的初始状态
    private LinkedList<State> states;//自动机中的若干状态
    private String name;//自动机的名字

    //构造器:空
    public Automata() {
        states = new LinkedList<State>();
        initState = new State("0");
        states.add(initState);//将初始状态添加进来
    }

    //构造器:初始状态名
    public Automata(String init) {
        states = new LinkedList<State>();
        initState = new State(init);
        states.add(initState);
    }

    //setter和getter

    public void setName(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    public State getInitState() {
        return initState;
    }

    public State getState(int index) {
        return states.get(index);
    }

    public void addState(State s) {
        states.add(s);
    }

    //创建新状态，加入状态链表，并返回
    public State newState() {
        State s = new State("" + nbOfStates());
        addState(s);
        return s;
    }

    //返回状态数目
    public int nbOfStates() {
        return states.size();
    }

    public LinkedList<State> getStates() {
        return states;
    }

    //获取本自动机中所有状态的转移数目
    public int nbOfTransitions() {
        int nb = 0;
        ListIterator<State> iterator = states.listIterator();
        while (iterator.hasNext()) {//对每个状态
            nb += iterator.next().nbOfTransitions();//获取转移数目
        }
        return nb;
    }

    //自动机字符串表示
    public String toAUT() {
        StringBuffer sb = new StringBuffer();
        //des(初始状态名,总转移数目,总状态数目)\n
        sb.append("des(" + initState.getName() + "," + nbOfTransitions() + "," + nbOfStates() + ")\n");
        ListIterator<State> iterator = states.listIterator();
        while (iterator.hasNext()) {//对自动机中每个状态
            //(本状态名, "转移值", 下一状态名)\n(本状态名, "转移值", 下一状态名)\n...
            sb.append(iterator.next().toAUT());
        }
        return new String(sb);
    }

    public void renameStates() {
        State s;
        int cpt = 0;
        ListIterator<State> iterator = states.listIterator();
        while (iterator.hasNext()) {
            s = iterator.next();
            s.setName("" + cpt);
            cpt++;
        }
    }
}