/** *****************************************************************************
 *
 * "FreePastry" Peer-to-Peer Application Development Substrate
 *
 * Copyright 2002-2007, Rice University. Copyright 2006-2007, Max Planck Institute
 * for Software Systems.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * - Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * - Neither the name of Rice  University (RICE), Max Planck Institute for Software
 * Systems (MPI-SWS) nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written
 * permission.
 *
 * This software is provided by RICE, MPI-SWS and the contributors on an "as is"
 * basis, without any representations or warranties of any kind, express or implied
 * including, but not limited to, representations or warranties of
 * non-infringement, merchantability or fitness for a particular purpose. In no
 * event shall RICE, MPI-SWS or contributors be liable for any direct, indirect,
 * incidental, special, exemplary, or consequential damages (including, but not
 * limited to, procurement of substitute goods or services; loss of use, data, or
 * profits; or business interruption) however caused and on any theory of
 * liability, whether in contract, strict liability, or tort (including negligence
 * or otherwise) arising in any way out of the use of this software, even if
 * advised of the possibility of such damage.
 *
 ****************************************************************************** */

package src;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import rice.environment.Environment;
import rice.p2p.commonapi.*;
import rice.p2p.commonapi.rawserialization.RawMessage;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.direct.*;
import rice.pastry.leafset.LeafSet;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;

/**
 * This tutorial shows how to setup a FreePastry node using the Socket Protocol.
 *
 * @author Jeff Hoye
 */
public class Tutorial {

    // this will keep track of our applications
    //Vector<MyApp> apps = new Vector<MyApp>();
    /**
     * This constructor launches numNodes PastryNodes. They will bootstrap to an
     * existing ring if one exists at the specified location, otherwise it will
     * start a new ring.
     *
     * @param bindport the local port to bind to
     * @param bootaddress the IP:port of the node to boot from
     * @param numNodes the number of nodes to create in this JVM
     * @param env the environment for these nodes
     * @param useDirect true for the simulator, false for the socket protocol
     */
    PastryNode node;
    LeafSet leafSet;
    FileApp app;
    Scanner scan = new Scanner(System.in);
    public Tutorial(int bindport, InetSocketAddress bootaddress, Environment env, JTextArea logText ) throws Exception {
        boolean useDirect = false;
        // Generate the NodeIds Randomly
        NodeIdFactory nidFactory = new RandomNodeIdFactory(env);

        // construct the PastryNodeFactory
        PastryNodeFactory factory;
        if (useDirect) {
            NetworkSimulator<DirectNodeHandle, RawMessage> sim = new EuclideanNetwork<DirectNodeHandle, RawMessage>(env);
            factory = new DirectPastryNodeFactory(nidFactory, sim, env);
        } else {
            factory = new SocketPastryNodeFactory(nidFactory, bindport, env);
        }

        IdFactory idFactory = new PastryIdFactory(env);

        Object bootHandle = null;

        // construct a node, passing the null boothandle on the first loop will cause the node to start its own ring
        node = factory.newNode();

        // construct a new FileApp
        app = new FileApp(node, idFactory, logText);

        // boot the node
        node.boot(bootaddress);

        // the node may require sending several messages to fully boot into the ring
        synchronized (node) {
            while (!node.isReady() && !node.joinFailed()) {
                // delay so we don't busy-wait
                node.wait(500);

                // abort if can't join
                if (node.joinFailed()) {
                    throw new IOException("Could not join the FreePastry ring.  Reason:" + node.joinFailedReason());
                }
            }
        }

        logText.append("Finished creating new node " + node+"\n");

        // wait 1 second
        env.getTimeSource().sleep(1000);

        // pick a node
        //FileApp app = apps.get(numNodes / 2);
        //PastryNode node = (PastryNode) app.getNode();
        // send directly to my leafset (including myself)
        

        // select the item
        //NodeHandle nh = leafSet.get(i);
        // send the message directly to the node
        //app.sendMyMsgDirect(nh);
        leafSet= node.getLeafSet();
        
    }


    public ArrayList<ItemCatalogo> lerCatalogo() {
        try {
            File xml = new File("catalogo.xml");
            
            ArrayList<ItemCatalogo> catalogo = new ArrayList<ItemCatalogo>();
            
            DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder db2 = dbFactory2.newDocumentBuilder();
            Document document = db2.parse(xml);

            NodeList list = document.getElementsByTagName("article");
            
            
            
            for (int i = 0; i < list.getLength(); i++) {
                org.w3c.dom.Node node = list.item(i);
                if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    ItemCatalogo item = new ItemCatalogo();
                    Element eElement = (Element) node;
                    int x = i + 1;
                    item.setAuthor(eElement.getElementsByTagName("author").item(0).getTextContent());
                    item.setFile(eElement.getElementsByTagName("file").item(0).getTextContent());
                    item.setJournal(eElement.getElementsByTagName("journal").item(0).getTextContent());
                    item.setNumber(eElement.getElementsByTagName("number").item(0).getTextContent());
                    item.setPages(eElement.getElementsByTagName("pages").item(0).getTextContent());
                    item.setPublisher(eElement.getElementsByTagName("publisher").item(0).getTextContent());
                    item.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
                    item.setVolume(eElement.getElementsByTagName("volume").item(0).getTextContent());
                    item.setYear(eElement.getElementsByTagName("year").item(0).getTextContent());
                    catalogo.add(item);
                }
            }

            return catalogo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Usage: java [-cp FreePastry-<version>.jar]
     * rice.tutorial.appsocket.Tutorial localbindport bootIP bootPort numNodes
     * or java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.Tutorial
     * -direct numNodes
     *
     * example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001
     * 10 example java rice.tutorial.DistTutorial -direct 10
     */
    public static void main(String[] args) throws Exception {
        try {

            boolean useDirect;
            if (args[0].equalsIgnoreCase("-direct")) {
                useDirect = true;
            } else {
                useDirect = false;
            }
            JTextArea logText = new JTextArea();
            // Loads pastry settings
            Environment env;
            if (useDirect) {
                env = Environment.directEnvironment();
            } else {
                env = new Environment();

                // disable the UPnP setting (in case you are testing this on a NATted LAN)
                env.getParameters().setString("nat_search_policy", "never");
            }

            int bindport = 0;
            InetSocketAddress bootaddress = null;

            if (!useDirect) {
                // the port to use locally
                bindport = Integer.parseInt(args[0]);

                // build the bootaddress from the command line args
                InetAddress bootaddr = InetAddress.getByName(args[1]);
                int bootport = Integer.parseInt(args[2]);
                bootaddress = new InetSocketAddress(bootaddr, bootport);
            }

            // launch our node!
            Tutorial dt = new Tutorial(bindport, bootaddress, env, logText);
        } catch (Exception e) {
            // remind user how to use
            System.out.println("Usage:");
            System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.Tutorial localbindport bootIP bootPort numNodes");
            System.out.println("  or");
            System.out.println("java [-cp FreePastry-<version>.jar] rice.tutorial.appsocket.Tutorial -direct numNodes");
            System.out.println();
            System.out.println("example java rice.tutorial.DistTutorial 9001 pokey.cs.almamater.edu 9001 10");
            System.out.println("example java rice.tutorial.DistTutorial -direct 10");
            throw e;
        }
    }
}
