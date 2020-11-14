import fs from 'fs';
import path from 'path';

import OrientalismBook from './templates/book';
import OrientalismSection from './templates/section';
import OrientalismCategory from './templates/category';

export default class Orientalism {
    static all(root, output) {
        const books = fs.readdirSync(root)
                        .filter(o => path.extname(o) === '.json')
                        .map(o => {
                            const json = JSON.parse(fs.readFileSync(path.resolve(root, o)))
                            const book = json.book;
                            json.categories = json.categories.map(o => {
                                const category = o.category;
                                o.sections = o.sections.map(o => {
                                    o.book = book;
                                    o.category = category;
                                    return o;
                                });
                                return o;
                            });

                            return json;
                        })
                        .sort((x, y) => x.seq - y.seq);

        books.forEach(o => {
            const book = o.book;
            const categories = o.categories;
            categories.forEach(o => {
                const category = o.category;
                const sections = o.sections;
                sections.forEach(o => {
                    const section = o.section;
                    OrientalismSection.gen({source:o, sections}, `${output}/${book}/${category}/${section}`);
                });
                OrientalismSection.gen({source: sections[0], sections}, `${output}/${book}/${category}/index`);
            });
            OrientalismCategory.gen({source: categories[0].sections[0], categories, sections: categories[0].sections}, `${output}/${book}/index`);
        });
        OrientalismBook.gen({source: books[0].categories[0].sections[0], books, sections: books[0].categories[0].sections}, `${output}/index`);
    }
}
