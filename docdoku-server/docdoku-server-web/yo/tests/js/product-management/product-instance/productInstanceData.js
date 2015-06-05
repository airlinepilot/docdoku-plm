/**
 * Created by kelto on 03/06/15.
 */
/*global casper,urls,products,productInstances*/

casper.test.begin('Product instance creation tests suite',19, function productInstanceCreationTestsSuite() {
    'use strict';

    casper.open('');

    /**
     * Open product management URL
     * */

    casper.then(function () {
        this.open(urls.productStructureForDeliverable);
    });



    /**
     * click on the checkbox from the bom
     */
    casper.then(function waitForBOM() {
        this.waitForSelector('.selectable-part-checkbox', function loadDataButton() {
            this.click('.selectable-part-checkbox');
            this.test.assert(true,'click on product-instance checkbox');
        }, function fail() {
            this.capture('screenshot/product-instance/FailToLoadDeliverable.png');
            this.test.assert(false,'could not load deliverable');
        });
    });

    /**
     * Wait for the Deliverable Data button and open the modal
     */
    casper.then(function openDataModal() {
        this.waitForSelector('#product_instance_btn', function openModal() {
            this.click('#product_instance_btn');
            this.test.assert(true, 'deliverable data button present');
            this.waitForSelector('.product-instance-data-modal', function waitForModal() {
                this.test.assert(true,'modal opened');
            }, function fail() {
                this.test.assert(false,'could not open modal');
            })
        }, function fail() {
            this.capture('screenshot/product-instance/NoDeliverableButton.png');
            this.test.assert(false, 'deliverable data button not present');
        });
    });

    /**
     * Count the attributes present in the modal
     */
    casper.then(function assertAttributePresent() {
        this.waitForSelector('#partAttributes', function countAttributes() {
            this.test.assertElementCount('#partAttributes .list-item', 2, '2 parts attributes present');
        }, function fail() {
            this.capture('screenshot/product-instance/CouldNotLoadPartAttributes.png');
            this.test.assert(false, 'could not load the part attributes');
        });
        this.waitForSelector('#pathDataAttributes', function countDataPath() {
            this.test.assertElementCount('#pathDataAttributes .list-item', 1, '1 data path attr present');
        }, function fail() {
            this.test.assert(false, 'could not load the data path attributes');
        });
    });

    /**
     * Count tab present in the modal
     */
    casper.then(function countTabPresent() {
        this.test.assertElementCount('ul.nav.nav-tabs li', 2, '2 tabs present in the modal');
    });

    /**
     * Add iteration note and save
     */
    casper.then(function addIterationNote() {
        this.sendKeys('.description-input', productInstances.productInstance1.iterationNote);
        this.click('.save-button');
        this.test.assertExists('#pathDataAttributes input.value:invalid',
            'should not create iteration without the mandatory attribute');
        this.sendKeys('#pathDataAttributes input.value', productInstances.productInstance1.pathDataValue);
        this.click('.save-button');
    });

    /**
     * re-open the modal
     */
    casper.then(function reopenModal() {
        this.waitWhileSelector('.product-instance-data-modal', function waitCloseModal() {
            this.waitForSelector('#product_instance_btn', function openModal() {
                this.click('#product_instance_btn');
                this.test.assert(true, 'deliverable data button present');
            }, function fail() {
                this.capture('screenshot/product-instance/NoDeliverableButton.png');
                this.test.assert(false, 'deliverable data button not present');
            });
        }, function fail() {
            this.capture('screenshot/product-instance/ModalNotClosing.png');
            this.test.assert(false, 'could not close the modal');
        });
    });

    /**
     * Now there should be 4 tab present and an iteration note.
     */
    casper.then(function countTab() {
        this.waitForSelector('#tab-attributes', function waitForModal(){
            this.test.assertElementCount('ul.nav.nav-tabs li', 4, '2 tabs present in the modal');
            this.test.assertExists('.product-instance-data-modal div.path-description');
            this.test.assertExists('input.description-input[value="'+productInstances.productInstance1.iterationNote+'"]');
            //Wait for the input value to be injected, can take sometime.
            this.waitForSelector('#pathDataAttributes input.value[value="'+productInstances.productInstance1.pathDataValue+'"]', function found() {
                this.test.assert(true,'the input value is given to the view');
            }, function fail() {
                this.test.assert(false, 'the previously given value is not printed in the input');
            });
        }, function fail() {
            this.capture('screenshot/product-instance/DeliverableDataModal-notFound.png');
            this.test.assert(false, 'deliverable data modal not found');
        });

    });


    /**
     * Go to the tab attributes
     */
    casper.then(function waitForModal() {
        this.waitForSelector('.product-instance-data-modal', function waitModal() {
            this.waitForSelector('#tab-attributes', function openAttributeTab() {
                this.click('ul.nav.nav-tabs li:nth-child(2) a');
                this.test.assert(true,'tab attribute loaded');
            }, function fail() {
                this.test.assert(false, 'could not load tab attribute');
            });
        }, function fail() {
            this.test.assert(false, 'could not open modal');
        });
    });

    /**
     * Test the attributes
     */
    casper.then(function testDataAttributes() {
        this.waitForSelector('ul.nav.nav-tabs li:nth-child(2).active', function() {
            this.test.assertElementCount('#partAttributes input.name[disabled]', 2,
                'the two part attributes name should be disabled in partAttributes');
            this.test.assertDoesntExist('#partAttributes input.value',
                'There should be no input for value in partAttributes');
            this.test.assertElementCount('#partAttributes div.controls.type', 2,
                'the type should be disabled for partAttributes');
            this.test.assertElementCount('#pathDataAttributes input.name[disabled]', 1,
                'the two part attributes name should be disabled for the pathDataAttributes');
            this.test.assertElementCount('#pathDataAttributes div.controls.type', 1,
                'the type should be disabled for pathDataAttributes');
            this.test.assertElementCount('#pathDataAttributes input.value[required]', 1,
                'the path data input for value should be required');
            this.click('.cancel-button');
        }, function fail() {
            this.capture('screenshot/product-instance/DeliverableTabAttributes-NotFound.png');
            this.test.assert(false, 'could not load the attribute tab');
        })

    });

    casper.run(function allDone() {
        this.test.done();
    });
});