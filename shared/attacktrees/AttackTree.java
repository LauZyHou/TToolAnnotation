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


package attacktrees;

import java.util.ArrayList;


/**
 * Class AttackTree
 * Creation: 10/04/2015
 *
 * @author Ludovic APVRILLE
 * @version 1.0 10/04/2015
 */
//攻击树
public class AttackTree extends AttackElement {
    private ArrayList<AttackNode> nodes;//攻击树的若干结点
    private ArrayList<Attack> attacks;//攻击树的若干攻击

    public AttackElement faultyElement;//攻击树的失败结点
    public String errorOfFaultyElement;//攻击树失败结点的error信息


    //构造器
    public AttackTree(String _name, Object _reference) {
        super(_name, _reference);
        nodes = new ArrayList<AttackNode>();
        attacks = new ArrayList<Attack>();
    }

    //添加结点和攻击

    public void addNode(AttackNode _node) {
        nodes.add(_node);
    }

    public void addAttack(Attack _attack) {
        attacks.add(_attack);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("List of nodes:");
        for (AttackNode an : nodes) {
            sb.append("  " + an.toString() + "\n");
        }
        return sb.toString();
    }

    public ArrayList<Attack> getAttacks() {
        return attacks;
    }

    public ArrayList<AttackNode> getAttackNodes() {
        return nodes;
    }

    // Checks:
    // Sequence/after/before nodes have attacks which are ordered (i.e. unique positive number)
    // Time value is positive in before and after
    // Attack name is unique
    // Node name is unique -> by construction, no need to check this
    public boolean checkSyntax() {
        // Negative order for attacks
        for (AttackNode an : nodes) {
            int faulty = an.hasNegativeAttackNumber();
            if (faulty >= 0) {
                faultyElement = an;
                errorOfFaultyElement = "Negative sequence number for node: " + an.getName() +
                        " and attack: " + an.getInputAttacks().get(faulty).getName();
                return false;
            }
        }


        // Order of input attacks : in sequence / after / before
        for (AttackNode an : nodes) {

            if ((an instanceof SequenceNode) || (an instanceof TimeNode)) {
                int faulty = an.hasUniqueAttackNumber();
                if (faulty >= 0) {
                    faultyElement = an;
                    errorOfFaultyElement = "Identical sequence number for node: " + an.getName() +
                            " and attack: " + an.getInputAttacks().get(faulty).getName();
                    return false;
                }
            }

        }

        // Time value is positive
        for (AttackNode an : nodes) {
            if (an instanceof TimeNode) {
                int t = ((TimeNode) an).getTime();
                if (t < 0) {
                    faultyElement = an;
                    errorOfFaultyElement = "Time value must be positive in: " + an.getName();
                    return false;
                }
            }
        }

        // Attack name is unique
        for (int i = 0; i < attacks.size() - 1; i++) {
            Attack atti = attacks.get(i);
            for (int j = i + 1; j < attacks.size(); j++) {
                //myutil.TraceManager.addDev("i=" + i + " j=" + j + " size=" + attacks.size());
                Attack attj = attacks.get(j);
                //myutil.TraceManager.addDev("i=" + atti.getName() + " j=" + attj.getName() + " size=" + attacks.size());
                if (atti.getName().compareTo(attj.getName()) == 0) {
                    faultyElement = atti;
                    errorOfFaultyElement = "Duplicate name for attack: " + atti.getName();
                    return false;
                }
            }
        }

        return true;
    }

}
